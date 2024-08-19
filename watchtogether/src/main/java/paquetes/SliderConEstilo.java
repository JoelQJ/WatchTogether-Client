package paquetes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class SliderConEstilo extends JSlider {


	private static final long serialVersionUID = 1L;
    public SliderConEstilo(int min, int max, int value) {
        super(min, max, value);
        
        
      
        setUI(new ElegantSliderUI(this));
        setOpaque(false);
        setPreferredSize(new Dimension(400, 60));
    }

  
    private static class ElegantSliderUI extends BasicSliderUI {


        public ElegantSliderUI(JSlider slider) {
            super(slider);
            slider.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                  
                   // slider.repaint(); //lag asique no

                   
                   
                }
            });
        }

        @Override
        public void paintThumb(Graphics g) {
            // Pintar la manija del slider (un pequeño círculo con borde)
            g.setColor(Color.WHITE);
            g.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
            g.setColor(Color.GRAY);
            g.drawOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
        }

        @Override
        public void paintTrack(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            int x = trackRect.x;
            int y = trackRect.y + trackRect.height / 2 - 4;
            int width = trackRect.width;
            int height = 8;

            // Dibujar la pista completa (color claro)
            g2d.setColor(new Color(200, 200, 200));
            g2d.fillRoundRect(x, y, width, height, height, height);

            // Dibujar la parte activa de la pista (color primario)
            int fillWidth = (int) (slider.getValue() / (double) slider.getMaximum() * width);
            g2d.setColor(new Color(0, 150, 255)); // Azul vibrante
            g2d.fillRoundRect(x, y, fillWidth, height, height, height);

           
        }

        @Override
        protected Dimension getThumbSize() {
            return new Dimension(15, 15); // Tamaño de la manija (thumb)
        }
    }


}
