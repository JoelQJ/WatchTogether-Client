package watchtogether;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import paquetes.SliderConEstilo;
import paquetes.tipos.PacketPause;
import paquetes.tipos.PacketSetMedia;
import paquetes.tipos.PacketSetTime;
import paquetes.tipos.PacketTerminado;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.AudioTrackInfo;
import uk.co.caprica.vlcj.media.TextTrackInfo;
import uk.co.caprica.vlcj.media.TrackInfo;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.fullscreen.windows.Win32FullScreenStrategy;

public class Player {

	private JFrame frame;
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private JSlider timeSlider;
	private JSlider volumenSlider;
	private JButton pauseButton;
	private JComboBox<Info> subtitleTrackSelector;
	private JComboBox<Info> audioTrackSelector;
	private JLabel tiempo;
	private MediaPlayer mediaPlayer;
	private boolean isFullScreen = false;
	private JPanel controlsPanel;
	private Timer stopTimer;
	private boolean pause = false;
	private JComponent[] componentesADesactivar;;

	public Player() {
		initializeFrame();
		initializeComponents();
		addComponentsToFrame();
		start();
	}

	private void initializeFrame() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		frame = new JFrame("Hiiragi Utena comparte anime " + Main.version);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1300, 600);
		frame.setIconImage(new ImageIcon(Main.class.getResource("/utena.png")).getImage());

	}

	private void initializeComponents() {
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent(mediaPlayerFactory, new Canvas(), // Video surface
																									// component
				new Win32FullScreenStrategy(frame), // Full screen strategy
				null, // Input events (use default)
				null // Overlay
		);

		controlsPanel = new JPanel(new BorderLayout()); // Change layout to BorderLayout
		timeSlider = new SliderConEstilo(0, 100, 0);
		timeSlider.setPreferredSize(new Dimension(1000, 20));
		timeSlider.setPaintLabels(true);
		timeSlider.setToolTipText("");
		timeSlider.setForeground(new Color(0, 0, 0));
		timeSlider.setToolTipText(null);

		volumenSlider = new SliderConEstilo(0, 0, 0);
		volumenSlider.setPreferredSize(new Dimension(500, 50));
		volumenSlider.setPaintLabels(true);
		volumenSlider.setToolTipText("");
		volumenSlider.setMinimum(1);
		volumenSlider.setMaximum(200);
		volumenSlider.setForeground(new Color(0, 0, 0));
		volumenSlider.setValue(30);
		volumenSlider.setPaintTicks(true);
		volumenSlider.setMinorTickSpacing(0);
		volumenSlider.setMajorTickSpacing(10);
		volumenSlider.setFocusable(false);
		pauseButton = new JButton("Play");
		subtitleTrackSelector = new JComboBox<>();
		Info infoCalcular = new Info(-99,"XXXXXXXXXXXXXXXXXXXXXXXXXXXXX","");
		subtitleTrackSelector.setPrototypeDisplayValue(infoCalcular);
		audioTrackSelector = new JComboBox<>();
		audioTrackSelector.setPrototypeDisplayValue(infoCalcular);


		tiempo = new JLabel();

		componentesADesactivar = new JComponent[] {pauseButton, timeSlider};
		
		stopTimer = new Timer(1000 * 10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (volumenSlider.getValueIsAdjusting() || timeSlider.getValueIsAdjusting() || subtitleTrackSelector.isPopupVisible() || audioTrackSelector.isPopupVisible()) {
					stopTimer.restart();
					return;
				}
				controlsPanel.setVisible(false);
				frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
						new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "InvisibleCursor"));
				stopTimer.stop();
			}
		});
		stopTimer.setRepeats(false);

		Action toggleFullScreenAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				toggleFullScreen();
			}
		};

		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "toggleFullScreen");
		frame.getRootPane().getActionMap().put("toggleFullScreen", toggleFullScreenAction);
	}

	private void addComponentsToFrame() {
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(mediaPlayerComponent, BorderLayout.CENTER);
		frame.getContentPane().add(controlsPanel, BorderLayout.SOUTH);

		JPanel downPanel = new JPanel();
		downPanel.add(pauseButton);
		downPanel.add(new JLabel("Subs:"));
		downPanel.add(subtitleTrackSelector);
		downPanel.add(new JLabel("Audio:"));
		downPanel.add(audioTrackSelector);
		downPanel.add(tiempo);
		downPanel.add(volumenSlider);
		controlsPanel.add(timeSlider, BorderLayout.NORTH); // Add top panel to north
		controlsPanel.add(downPanel, BorderLayout.SOUTH); // Add slider to south

		frame.revalidate();
	}

	private void start() {
		frame.setVisible(true);
		frame.setFocusable(true);
		frame.requestFocus();

		stopTimer.start();

		MouseMotionListener mouseEvent = new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				controlsPanel.setVisible(true);
				stopTimer.restart();
				frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				super.mouseMoved(e);
			}
		};

		mediaPlayerComponent.videoSurfaceComponent().addMouseMotionListener(mouseEvent);
		frame.addMouseMotionListener(mouseEvent);

		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				mediaPlayer = mediaPlayerComponent.mediaPlayer();
				mediaPlayer.audio().setVolume(volumenSlider.getValue());

				pauseButton.addActionListener(ev -> {
					Main.cliente.enviarPaquete(new PacketPause(!pause).toString());
					setPausar(!pause);

				});

				mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

					@Override
					public void finished(MediaPlayer mediaPlayer) {
						Main.cliente.enviarPaquete(new PacketTerminado().toString());
					}
					
					@Override
					public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
						SwingUtilities.invokeLater(() -> {
							long length = mediaPlayer.status().length();
							if (length > 0) {
								if (!timeSlider.hasFocus())
									timeSlider.setValue((int) newTime);

								if (controlsPanel.isVisible()) {
									int segundos = (int) (mediaPlayer.status().time() / 1000);
									tiempo.setText(String.format("%02d:%02d:%02d", (segundos / 3600),
											(segundos % 3600) / 60, segundos % 60));
								}
							}
						});
					}

					@Override
					public void mediaPlayerReady(MediaPlayer mediaPlayer) {
						loadSubtitleTracks();
						loadAudioTracks();
						setupSlider();
						controlsPanel.updateUI();
						setPausar(true);
						setTime(0);
						volumenSlider.repaint();
						mediaPlayer.audio().setVolume(volumenSlider.getValue());
						Main.cliente.enviarPaquete(new PacketSetMedia().toString());
					}
				});

				subtitleTrackSelector.addActionListener(ev -> {
					Info selectedInfo = (Info) subtitleTrackSelector.getSelectedItem();
					if (selectedInfo != null )
						mediaPlayer.subpictures().setTrack(selectedInfo.getIndice());
					
						
				});

				audioTrackSelector.addActionListener(ev -> {
					Info selectedAudio = (Info) audioTrackSelector.getSelectedItem();
					if (selectedAudio != null)
						mediaPlayer.audio().setTrack(selectedAudio.getIndice());
				});

				// Actualizar la posición del video según la barra de tiempo
				timeSlider.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						SwingUtilities.invokeLater(() -> {
							if (!timeSlider.getValueIsAdjusting() && timeSlider.hasFocus()) {
								long length = mediaPlayer.status().length();
								if (length > 0) {
									long newTime = timeSlider.getValue();
									mediaPlayer.controls().setTime(newTime);
									Main.cliente.enviarPaquete(new PacketSetTime(newTime).toString());
									timeSlider.setFocusable(false);
									timeSlider.setFocusable(true);
								}
							}
						});
					}
				});

				volumenSlider.addChangeListener((change) -> {

					mediaPlayer.audio().setVolume(volumenSlider.getValue());
					volumenSlider.setToolTipText(volumenSlider.getValue() + "%");

				});
			}
		});

	}

	public void terminar() {
		setPlayPrimeraVez();
		setTime(mediaPlayer.status().length()-10);
	}
	
	public void setPausar(boolean pausar) {
		SwingUtilities.invokeLater(() -> {
			mediaPlayer.controls().setPause(pausar);
			pause = pausar;
			if (!pausar) {
				pauseButton.setText("Pausar");
			} else
				pauseButton.setText("Play");
		});
	}
	
	public void setPlayPrimeraVez() {
		setPausar(false);
		for(JComponent componente : componentesADesactivar) {
			componente.setEnabled(true);
			componente.setToolTipText(null);
		}
		
	}
	
	public void setMedia(String path) {
		SwingUtilities.invokeLater(() -> {
			mediaPlayer.media().play(path);
			for(JComponent componente : componentesADesactivar) {
				componente.setEnabled(false);
				componente.setToolTipText("No puedes usar esto hasta que comience la reproduccion");
			}

		});
	}


	private void loadSubtitleTracks() {
		List<? extends TrackInfo> tracks = mediaPlayer.media().info().tracks();
		subtitleTrackSelector.removeAllItems();
		System.out.println("\n[Player] Cargando Subtitulos...");
		subtitleTrackSelector.addItem(new Info(-1, "Desactivar Subtitulos",""));
		Info subLatinAmerican = null;
		for (int i = 0; i < tracks.size(); i++) {
			if (tracks.get(i) instanceof TextTrackInfo) {
				TextTrackInfo track = (TextTrackInfo) tracks.get(i);
				String idioma = track.language() == null ? "" : track.language();
				String description = track.description() == null ? idioma : track.description();
				
				if (description != null && !description.isEmpty()) {
					Info texto = new Info(i, description, idioma);
					subtitleTrackSelector.addItem(texto);
					if(!description.toLowerCase().contains("commentary"))
						if(subLatinAmerican == null && !description.toLowerCase().contains("forced") && comprobarIdiomaEnDesc(description, idioma, "latin","spa","european"))
							subLatinAmerican = texto;
					String debugTexto = subLatinAmerican == texto ? String.format("%s <-- Elegido como Subtitulo", texto.debug()) : texto.debug();
					System.out.println(debugTexto);
				}
			}
			
		}
		System.out.println("\n");
		
		if (subtitleTrackSelector.getItemCount() > 0) {
			if (subLatinAmerican != null) {
				subtitleTrackSelector.setSelectedItem(subLatinAmerican);
				mediaPlayer.subpictures().setTrack(subLatinAmerican.getIndice());
			} else {
				subtitleTrackSelector.setSelectedIndex(0);
				mediaPlayer.subpictures().setTrack(0);
			}
		}
	}

	private boolean comprobarIdiomaEnDesc(String desc, String idioma, String... esperados){
		List<String> esperadosLista = Arrays.asList(esperados);
		return esperadosLista.contains(desc.toLowerCase()) || esperadosLista.contains(idioma.toLowerCase());
	}

	private void loadAudioTracks() {
		List<? extends TrackInfo> tracks = mediaPlayer.media().info().tracks();
		audioTrackSelector.removeAllItems();
		Info audioJapones = null;
		System.out.println("[Player] Cargando Audios...");
		for (int i = 0; i < tracks.size(); i++) {
			if (tracks.get(i) instanceof AudioTrackInfo) {
				AudioTrackInfo track = (AudioTrackInfo) tracks.get(i);
				String idioma = track.language() == null ? "" : track.language();
				String description = track.description() == null || track.description().isEmpty() ? idioma : track.description();
				if (description != null && !description.isEmpty()) {
					Info audio = new Info(i, description, idioma);
					audioTrackSelector.addItem(audio);
					if(!description.toLowerCase().contains("commentary"))
						if(comprobarIdiomaEnDesc(description, idioma, "ja", "jpn")){
							audioJapones = audio;
							audioJapones.setNombre("Japonés - 日本語");
						}
					
					String debugTexto = audioJapones == audio ? String.format("%s <-- Elegido como Audio", audio.debug()) : audio.debug();
					System.out.println(debugTexto);
				}
			}
		}
		System.out.println("\n");
		if (audioTrackSelector.getItemCount() > 0) {
			if (audioJapones != null) {
				audioTrackSelector.setSelectedItem(audioJapones);
				mediaPlayer.audio().setTrack(audioJapones.getIndice());
			} else {
				audioTrackSelector.setSelectedIndex(0);
				mediaPlayer.audio().setTrack(0);
			}
		}
	}

	private void setupSlider() {
		timeSlider.setMaximum((int) mediaPlayer.status().length());
		timeSlider.setValue((int) mediaPlayer.status().time());


	}

	private void toggleFullScreen() {
		if (isFullScreen) {
			mediaPlayerComponent.mediaPlayer().fullScreen().set(false);
			// frame.setUndecorated(false);
			frame.setResizable(true);
			frame.setVisible(true);
		} else {
			// frame.setUndecorated(true);
			frame.setResizable(false);
			mediaPlayerComponent.mediaPlayer().fullScreen().set(true);
		}
		isFullScreen = !isFullScreen;
	}

	public void setTime(long time) {
		SwingUtilities.invokeLater(() -> {
			mediaPlayer.controls().setTime(time);
		});
	}
}
