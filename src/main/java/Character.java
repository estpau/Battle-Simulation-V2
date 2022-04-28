public abstract class Character{

    private int id;
    private String name;
    private int hp;
    private boolean isAlive;

    private int currentID = 0;

    public Character(String name, int hp) {
        this.id = currentID;
        this.name = name;
        this.hp = hp;
        this.isAlive = true;
        currentID++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    abstract public void attack(Character p);
}
