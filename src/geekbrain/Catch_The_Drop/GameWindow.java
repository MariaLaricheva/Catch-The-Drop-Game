package geekbrain.Catch_The_Drop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class GameWindow extends JFrame {
    private static GameWindow game_window;

    private static Image background;
    private static Image game_over;
    private static Image drop;
    private static float drop_left = 200;
    private static float drop_top = -200;

    private static long last_frame_time;

    private static float drop_vel = 200;

    private static int score = 0;


    public static void main(String[] args) throws IOException {
        background = ImageIO.read(Objects.requireNonNull(GameWindow.class.getResourceAsStream("static/background.png")));
        game_over = ImageIO.read(Objects.requireNonNull(GameWindow.class.getResourceAsStream("static/game_over.png")));
        BufferedImage bufferedDrop = ImageIO.read(Objects.requireNonNull(GameWindow.class.getResourceAsStream("static/drop.png")));
        drop = bufferedDrop.getScaledInstance(100, 160, Image.SCALE_DEFAULT);
        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // при закрытии окна программа завершается
        game_window.setLocation(225,20);
        game_window.setSize(1280,897); //под новую картинку
        game_window.setResizable(false);
        last_frame_time = System.nanoTime();
        GameField game_field = new GameField();

        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //super.mousePressed(e);
                int x = e.getX();
                int y = e.getY();
                float drop_right = drop_left + drop.getWidth(null);
                float drop_bottom = drop_top + drop.getHeight(null);
                boolean is_drop = x >= drop_left && x <= drop_right && y <= drop_bottom && y >= drop_top;
                    if (is_drop) {
                        drop_top = -200;
                        drop_left = (int)(Math.random() * (game_field.getWidth() - drop.getWidth(null)));
                        drop_vel += 10;
                        score++;
                        game_window.setTitle("Score: " + score);
                    }

            }
        });

        game_window.add(game_field);
        game_window.setVisible(true);
    }


    private static void onRepaint(Graphics g){
        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;
        drop_top += drop_vel * delta_time;
        g.drawImage(background, 0, 0, null);
        g.drawImage(drop, (int)drop_left, (int)drop_top, null);
        if (drop_top >= game_window.getHeight()) {
            g.drawImage(game_over, 100, -100, null);
        }
    }


    private static class GameField extends JPanel {

        @Override //меняем поведение класса JPanel на то, что нужно нам для задачи
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // через слово super мы получаем доступ к родительскому классу
            onRepaint(g);
            repaint();
        }


    }
}
