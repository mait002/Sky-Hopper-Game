import javax.swing.*;

public class App{
    public static void main(String[] args) throws Exception{
        int boardwidth = 360;
        int boardheight = 640;

        

        JFrame frame = new JFrame("Sky Hopper");
        // frame.setVisible(true);
        frame.setSize(boardwidth, boardheight);
        frame.setLocationRelativeTo(null); //will place window at the centre of our screen
        frame.setResizable(false); //so user cannot resize the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when user clicks x button on the window, it will terminate the program

        SkyHopper skyhopper = new SkyHopper();
        frame.add(skyhopper);
        frame.pack(); // it prevents from the width and height from including the title bar, thus now frame is 360x640 excluding title bar
        skyhopper.requestFocus();
        frame.setVisible(true);
        
    }
}