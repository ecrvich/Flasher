package org.ecrvich.Flasher;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

/**
 * Flash color changes at a user-defined speed. Offers both black-and-white as
 * well as full-color mode.
 * 
 * @author Ernest Crvich
 * 
 */
public class Flasher
      extends JFrame
      implements Runnable, WindowListener
{
   private static final long serialVersionUID = 4086479189444131539L;
   int speed = 25;
   JComponent jcomp;
   Random randColor = new Random( System.currentTimeMillis() );
   boolean colorMode = false;
   Color curBack = Color.white;
   JPanel panel;
   Runner runner;
   JLabel speedLabel;

   class Runner
         extends Thread
   {
      boolean running = false;
      boolean stopping = false;

      public void quit()
      {
         this.stopping = true;
      }

      public void pause()
      {
         this.running = false;
      }

      public void restart()
      {
         this.running = true;
      }

      @Override
      public void run()
      {
         while (!this.stopping)
         {
            if (this.running)
            {
               if (Flasher.this.colorMode)
                  Flasher.this.curBack = new Color( Flasher.this.randColor
                        .nextInt() );
               else if (Flasher.this.curBack == Color.black)
                  Flasher.this.curBack = Color.white;
               else
                  Flasher.this.curBack = Color.black;
               setColor( Flasher.this.curBack );
            }

            try
            {
               Thread.sleep( 1000 / getDivisor() );
            }
            catch (InterruptedException e)
            {
               // ignore
            }
         }
      }
      
      private int getDivisor()
      {
         int result = Flasher.this.speed;
         Random randColor = new Random( System.currentTimeMillis() );
         return (result);
      }
   }

   public Flasher()
   {
      super( "Flasher" );
      setupGUI();
      this.runner = new Runner();
      this.runner.start();
   }

   public void setupGUI()
   {
      Dimension dims = Toolkit.getDefaultToolkit().getScreenSize();
      this.panel = new JPanel( new GridLayout( 50, 4 ) );
      this.speedLabel = new JLabel( "" + this.speed );
      this.panel.add( this.speedLabel );
      this.setJMenuBar( setupMenu() );
      this.setBounds( new Rectangle( 0, 0, (int)dims.getWidth(), (int)dims
            .getHeight() ) );
      this.getContentPane().add( this.panel, BorderLayout.CENTER );
      this.addWindowListener( this );
      setColor( this.curBack );
   }

   JMenuBar setupMenu()
   {
      JMenuBar menuBar;
      JMenu ctlMenu, setMenu;
      JMenuItem menuItem;
      JRadioButtonMenuItem rbMenuItem;

      menuBar = new JMenuBar();
      ctlMenu = new JMenu( "Control" );
      setMenu = new JMenu( "Settings" );
      menuBar.add( ctlMenu );
      menuBar.add( setMenu );

      menuItem = new JMenuItem( "Start" );
      menuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_S,
            ActionEvent.CTRL_MASK ) );
      menuItem.addActionListener( new ActionListener()
      {
         public void actionPerformed( ActionEvent e )
         {
            Flasher.this.runner.restart();
         }
      } );
      ctlMenu.add( menuItem );

      menuItem = new JMenuItem( "Stop" );
      menuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_E,
            ActionEvent.CTRL_MASK ) );
      menuItem.addActionListener( new ActionListener()
      {
         public void actionPerformed( ActionEvent e )
         {
            Flasher.this.runner.pause();
            Flasher.this.setColor( Color.white );
         }
      } );
      ctlMenu.add( menuItem );

      ctlMenu.addSeparator();

      menuItem = new JMenuItem( "Slower" );
      menuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_MINUS,
            ActionEvent.CTRL_MASK ) );
      menuItem.addActionListener( new ActionListener()
      {
         public void actionPerformed( ActionEvent e )
         {
            if (Flasher.this.speed > 1)
               --Flasher.this.speed;
            Flasher.this.speedLabel.setText( "" + Flasher.this.speed );
         }
      } );
      ctlMenu.add( menuItem );

      menuItem = new JMenuItem( "Faster" );
      menuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_EQUALS,
            ActionEvent.CTRL_MASK ) );
      menuItem.addActionListener( new ActionListener()
      {
         public void actionPerformed( ActionEvent e )
         {
            ++Flasher.this.speed;
            Flasher.this.speedLabel.setText( "" + Flasher.this.speed );
         }
      } );
      ctlMenu.add( menuItem );

      ButtonGroup group = new ButtonGroup();
      rbMenuItem = new JRadioButtonMenuItem( "B&W" );
      rbMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_B,
            ActionEvent.CTRL_MASK ) );
      rbMenuItem.setSelected( true );
      rbMenuItem.setMnemonic( KeyEvent.VK_R );
      group.add( rbMenuItem );
      rbMenuItem.addActionListener( new ActionListener()
      {
         public void actionPerformed( ActionEvent e )
         {
            Flasher.this.colorMode = false;
         }
      } );
      setMenu.add( rbMenuItem );

      rbMenuItem = new JRadioButtonMenuItem( "Color" );
      rbMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_C,
            ActionEvent.CTRL_MASK ) );
      rbMenuItem.setMnemonic( KeyEvent.VK_O );
      group.add( rbMenuItem );
      rbMenuItem.addActionListener( new ActionListener()
      {
         public void actionPerformed( ActionEvent e )
         {
            Flasher.this.colorMode = true;
         }
      } );
      setMenu.add( rbMenuItem );

      return (menuBar);
   }

   void setColor( Color back )
   {
      this.panel.setBackground( back );
      this.speedLabel.setText( "" + this.speed );
   }

   public void run()
   {
      setVisible( true );
   }

   public void windowActivated( WindowEvent e )
   {
      // nothing yet
   }

   public void windowClosed( WindowEvent e )
   {
      // nothing yet
   }

   public void windowClosing( WindowEvent e )
   {
      this.runner.quit();
      dispose();
   }

   public void windowDeactivated( WindowEvent e )
   {
      // nothing yet
   }

   public void windowDeiconified( WindowEvent e )
   {
      // nothing yet
   }

   public void windowIconified( WindowEvent e )
   {
      // nothing yet
   }

   public void windowOpened( WindowEvent e )
   {
      // nothing yet
   }

   public static void main( String[] args )
   {
      SwingUtilities.invokeLater( new Flasher() );
   }
}
