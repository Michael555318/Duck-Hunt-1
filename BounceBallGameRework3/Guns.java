import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public interface Guns
{
    void paint(Graphics g);
    void aim(Graphics g, int x, int y);
}
