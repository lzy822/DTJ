import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

final class MyFileChooser extends JFileChooser {
    private final MyFilterWrapper filter;

    public MyFileChooser(MyFilterWrapper filter) {
        this.filter = filter;
        // 扩展名过滤
        setFileFilter(filter);

        // 文件选择属性设置
        setMultiSelectionEnabled(true);
        setAcceptAllFileFilterUsed(false);
        setFileSelectionMode(FILES_AND_DIRECTORIES);
    }

    public String [] getAbsolutePathsRecursively() {
        ArrayList<String> paths = new ArrayList<String>();
        File [] files = getSelectedFiles();
        traverse(files, paths);
        return paths.toArray(new String [] {});
    }

    private void traverse(File [] files, ArrayList<String> paths) {
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                traverse(f.listFiles(this.filter), paths);
            } else if (f.isFile() && this.filter.accept(f)) {
                paths.add(f.getAbsolutePath());
            }
        }
    }
}

final class MyFilterWrapper extends javax.swing.filechooser.FileFilter implements java.io.FileFilter {
    private final FileNameExtensionFilter filter;

    public MyFilterWrapper(String description, String... extensions) {
        this.filter = new FileNameExtensionFilter(description, extensions);
    }

    public boolean accept(File f) {
        return this.filter.accept(f);
    }

    public String getDescription() {
        return this.filter.getDescription();
    }
}

class ZoomablePicture extends JComponent {
    private Image image;
    private int width, height;
    private float zoomFactor;
    private float SumZoomFactor;

    public void load(String filename) {
        unload();
        image = Toolkit.getDefaultToolkit().getImage(filename);
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(image, 0);
        try {
            mt.waitForAll();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        width = image.getWidth(null);
        height = image.getHeight(null);
        zoomFactor = 1.0f;
        SumZoomFactor = 1.0f;
        System.out.println(getWidth() + ", " + getHeight());
        setPreferredSize(new Dimension(width, height));
        revalidate();
        repaint();


        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getPoint() + "\n" + e.getLocationOnScreen());
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                Point oldMousePt = e.getPoint();
                double xRate = oldMousePt.getX() / getWidth();
                double yRate = oldMousePt.getY() / getHeight();
                int xOffset = (int)(Math.round(oldMousePt.getX() - getVisibleRect().getLocation().getX()));
                int yOffset = (int)(Math.round(oldMousePt.getY() - getVisibleRect().getLocation().getY()));

                System.out.println(getWidth() + ", " + getHeight());
                if (e.getWheelRotation() == 1)
                {
                    SumZoomFactor*=1.1f;
                    setZoomFactor(zoomFactor*1.1f);
                }
                else
                {
                    float newZoomFactor = zoomFactor*0.9f;
                    if (newZoomFactor < 1)
                    {
                        setZoomFactor(1);
                        SumZoomFactor = 1;
                    }
                    else
                    {
                        setZoomFactor(newZoomFactor);
                        SumZoomFactor*=0.9f;
                    }
                }
                ZoomForMouseCenter(xRate, yRate, xOffset, yOffset);
            }
        });

    }

    private void ZoomForMouseCenter(double xRate, double yRate, int xOffset, int yOffset){
        Rectangle rect = getVisibleRect();
        rect.setLocation(new Point((int)Math.round(getPreferredSize().width * xRate-(xOffset)), (int)Math.round(getPreferredSize().height * yRate-(yOffset))));
        scrollRectToVisible(rect);
    }

    public void unload() {
        if (image != null) {
            image = null;
            setPreferredSize(new Dimension(1, 1));
            revalidate();
            repaint();
        }
    }

    public void setZoomFactor(float factor) {
        zoomFactor = factor;
        setPreferredSize(new Dimension((int) (width * zoomFactor), (int) (height * zoomFactor)));
        revalidate();
        repaint();
    }

    public float getZoomFactor() {
        return zoomFactor;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            int ws = getWidth();
            int hs = getHeight();
            int wp = getPreferredSize().width;
            int hp = getPreferredSize().height;
            g.drawImage(image, (ws - wp) / 2, (hs - hp) / 2, wp, hp, null);
        }
    }
}

class ScrollablePicture extends ZoomablePicture {
    private Point oldCursorPos;

    public ScrollablePicture() {
        addMouseMotionListener(new MouseMotionAdapter () {
            public void mouseDragged(MouseEvent e) {
                dragTo(e.getLocationOnScreen());
            }
        });
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                startDragging(e.getLocationOnScreen());
            }
            public void mouseReleased(MouseEvent e) {
                stopDragging();
            }
        });
    }

    public void load(String filename) {
        super.load(filename);
        scrollRectToVisible(new Rectangle()); // 滚动到左上角位置
    }

    private void startDragging(Point cursorPos) {
        oldCursorPos = cursorPos;
        setCursor(new Cursor(Cursor.MOVE_CURSOR));
    }

    private void stopDragging() {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private void dragTo(Point newCursorPos) {
        int dx = newCursorPos.x - oldCursorPos.x;
        int dy = newCursorPos.y - oldCursorPos.y;
        Rectangle visibleRect = getVisibleRect();
        visibleRect.translate(-dx, -dy);
        scrollRectToVisible(visibleRect);
        oldCursorPos = newCursorPos;
    }
}

class ToolBarStatusFrame extends JFrame {
    private final JToolBar toolbar = new JToolBar();
    private final JLabel status = new JLabel();

    public ToolBarStatusFrame() {
        Container cp = getContentPane();
        cp.add(toolbar, BorderLayout.NORTH);
        cp.add(status, BorderLayout.SOUTH);
    }

    public void setToolBarComponentsEnabled(boolean... enabled) {
        for (int i = 0; i < enabled.length; i++) {
            toolbar.getComponent(i).setEnabled(enabled[i]);
        }
    }

    public void addToolBarComponents(JComponent... comp) {
        for (int i = 0; i < comp.length; i++) {
            toolbar.add(comp[i]);
        }
    }

    public void setStatus(String statusText) {
        status.setText(statusText);
    }
}

final class MyPicViewer extends ToolBarStatusFrame {
    private JButton btn_winTopButton;
    private ZoomablePicture view = new ScrollablePicture();

    public static void main(String [] args) {
        final List<Map> mapList = new ArrayList<>();
        mapList.add(new Map("", "序图组", 0, ""));
        mapList.add(new Map("", "资源与环境图组", 0, ""));
        mapList.add(new Map("", "社会经济图组", 0, ""));
        mapList.add(new Map("", "区域地理图组", 0, ""));
        mapList.add(new Map("序图组", "世界地图", 1, ""));
        mapList.add(new Map("序图组", "中国政区", 1, ""));
        mapList.add(new Map("资源与环境图组", "人口劳动力", 2, ""));
        mapList.add(new Map("社会经济图组", "综合经济", 3, ""));
        mapList.add(new Map("区域地理图组", "县图", 4, ""));
        mapList.add(new Map("区域地理图组", "各县城区图", 4, ""));
        mapList.add(new Map("区域地理图组", "各县影像图", 4, ""));
        mapList.add(new Map("区域地理图组", "乡镇图", 4, ""));
        mapList.add(new Map("世界地图", "test", 5, "D:\\\\test.png"));
        System.out.println(mapList.size());
        String [] nameStd = {"序图组","资源与环境图组","社会经济图组","区域地理图组"};
        Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(scrSize.getWidth() + ", " + scrSize.getHeight());
        JFrame main_frame = new JFrame();
        LoadNextMapType(main_frame, mapList, FindNextMapList(mapList, ""));
        //new MyPicViewer("D:\\test.png");
    }

    private void ss(){
        final List<Map> mapList = new ArrayList<>();
        String [] nameStd = {"序图组","资源与环境图组","社会经济图组","区域地理图组"};
        Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(scrSize.getWidth() + ", " + scrSize.getHeight());
        JFrame main_frame = new JFrame();
        main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main_frame.setTitle("临沧市地图集");
        main_frame.setLayout(new FlowLayout());
        main_frame.setSize(260,280);
        main_frame.setLocation(scrSize.width/2 - 130,scrSize.height/2 - 140);
        for(int i=0;i<4;i++)
        {
            final String friend_name=nameStd[i];
            JButton now = new JButton(friend_name);
            now.setPreferredSize(new Dimension(200,50));
            main_frame.add(now);
            // chose someone to talk
            ActionListener listener = new ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                    main_frame.getContentPane().removeAll();
                    main_frame.repaint();
                    main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    main_frame.setTitle("临沧市地图集");
                    main_frame.setLayout(new FlowLayout());
                    main_frame.setSize(260,280);
                    main_frame.setLocation(scrSize.width/2 - 130,scrSize.height/2 - 140);
                    for (int j = 0; j < mapList.size(); j++) {
                        Map m = mapList.get(j);
                        if (m.getParentNode().equals(friend_name)){
                            System.out.println(friend_name);
                            JButton now = new JButton(m.getName());
                            now.setPreferredSize(new Dimension(200,50));
                            main_frame.add(now);
                            // chose someone to talk
                            ActionListener listener = new ActionListener()
                            {
                                public void actionPerformed(ActionEvent event)
                                {
                                    /*main_frame.removeAll();
                                    for (int j = 0; j < mapList.size(); j++) {
                                        Map m = mapList.get(j);
                                        if (m.getParentNode().equals(friend_name)){

                                        }
                                    }*/
                                }
                            };
                            now.addActionListener(listener);
                        }
                    }
                    main_frame.repaint();
                    main_frame.setVisible(true);
                }
            };
            now.addActionListener(listener);
        }

        main_frame.setVisible(true);
    }

    static private void LoadNextMapType(JFrame main_frame, List<Map> maps, List<Map> currentMaps){
        int size = currentMaps.size();
        if (size == 1 && !currentMaps.get(0).getPath().isEmpty()) {
            main_frame.setEnabled(false);
            new MyPicViewer(currentMaps.get(0).getPath(), main_frame, currentMaps.get(0).getName());
        }
        else if (size == 0) {

        }
        else {
            int currentMapType = currentMaps.get(0).getMapType();
            main_frame.getContentPane().removeAll();
            Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
            main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            main_frame.setLayout(new FlowLayout());
            for (int i = 0; i < size; i++) {
                final String btName = currentMaps.get(i).getName();
                JButton now = new JButton(btName);
                now.setPreferredSize(new Dimension(200, 50));
                main_frame.add(now);
                ActionListener listener = new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        List<Map> newMaps = FindNextMapList(maps, btName);
                        LoadNextMapType(main_frame, maps, newMaps);
                    }
                };
                now.addActionListener(listener);
            }
            int currentHeight = size * 50 + 80;
            main_frame.setTitle("临沧市地图集");
            if (currentMapType > 0) {
                main_frame.setTitle(currentMaps.get(0).getParentNode());
                currentHeight = currentHeight + 50;
                JButton now = new JButton("返回");
                now.setPreferredSize(new Dimension(200, 50));
                main_frame.add(now);
                ActionListener listener = new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        List<Map> newMaps = FindParentMapList(maps, currentMaps.get(0).getParentNode());
                        System.out.println(newMaps.size() + ", " + currentMaps.get(0).getParentNode());
                        main_frame.getContentPane().removeAll();
                        LoadNextMapType(main_frame, maps, newMaps);
                    }
                };
                now.addActionListener(listener);
            }
            main_frame.setSize(260, currentHeight);
            main_frame.setLocation(scrSize.width / 2 - 130, scrSize.height / 2 - currentHeight / 2);
            main_frame.repaint();
            main_frame.setVisible(true);
        }
    }

    static private List<Map> FindParentMapList(List<Map> maps, String parentNodeName){
        List<Map> newMaps = new ArrayList<>();
        int MapType = -1;
        for (int i = 0; i < maps.size(); i++) {
            if (maps.get(i).getName().equals(parentNodeName))
                MapType = maps.get(i).getMapType();
        }

        System.out.println(MapType);

        for (int i = 0; i < maps.size(); i++) {
            if (maps.get(i).getMapType() == MapType)
                newMaps.add(maps.get(i));
        }
        return newMaps;
    }

    static private List<Map> FindNextMapList(List<Map> maps, String parentNodeName){
        List<Map> newMaps = new ArrayList<>();
        for (int i = 0; i < maps.size(); i++) {
            if (maps.get(i).getParentNode().equals(parentNodeName))
                newMaps.add(maps.get(i));
        }
        return newMaps;
    }

    public MyPicViewer(String path, JFrame frame, String name) {
        setTitle(name);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        createToolBarButtons();
        //setToolBarComponentsEnabled(true, false, false, false, false, true);

        getContentPane().add(new JScrollPane(view));

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                //
            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
                frame.setEnabled(true);
                frame.setVisible(true);
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        showCurrentPicture(path);
        Image image = Toolkit.getDefaultToolkit().getImage(path);
        setSize(image.getWidth(null), image.getHeight(null));

        setVisible(true);

        /*this.pictureList = fc.getAbsolutePathsRecursively();
        this.pictureIndex = (this.pictureList.length > 0) ? 0 : -1;*/
    }

    private class ToolbarButton extends JButton {
        public ToolbarButton(String text, String icon, ActionListener l) {
            super(text, new ImageIcon(MyPicViewer.this. getClass().getClassLoader().getResource(icon)));
            addActionListener(l);
            setPreferredSize(new Dimension(100, 21));
        }
    }

    private void createToolBarButtons() {
        btn_winTopButton = new ToolbarButton("窗口置顶", "icons/icon_top.png", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setAlwaysOnTop(!isAlwaysOnTop());
                if(isAlwaysOnTop()){
                    btn_winTopButton.setText("取消置顶");

                }
                else{
                    btn_winTopButton .setText("窗口置顶");
                }
            }
        });

        addToolBarComponents(btn_winTopButton);
    }

    private void showCurrentPicture(String path) {
        String filename = path;
        System.out.println(filename);
        this.view.load(filename);
        //setStatus(String.format("[%d/%d] %s", i + 1, this.pictureList.length, filename));
        //setToolBarComponentsEnabled(true, i >= 0, i >= 0, i > 0, i + 1 < this.pictureList.length, true);
    }
}

