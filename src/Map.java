public class Map {
    private String parentNode;
    private String name;
    private int mapType;
    private String path;

    public Map(String parentNode, String name, int mapType, String path) {
        this.parentNode = parentNode;
        this.name = name;
        this.mapType = mapType;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParentNode() {
        return parentNode;
    }

    public void setParentNode(String parentNode) {
        this.parentNode = parentNode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }
}
