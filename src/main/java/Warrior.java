public class Warrior extends  Character{

    private int stamina;
    private int strength;

    public Warrior(String name, int hp, int stamina, int strength) {
        super(name, hp);
        this.stamina = stamina;
        this.strength = strength;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }


    @Override
    public void attack(Character p) {
        //strong attack
        System.out.print("With stamina " + this.stamina + " and strength " + this.strength);
        if (this.stamina >= 5){
            System.out.println(" " + this.getName() + " has done a strong attack");
            p.setHp(p.getHp() - this.strength);
            setStamina(stamina-5);
            if (p.getHp() <= 0){
                p.setAlive(false);
                p.setHp(0);
            }
            //weak attack
        }else{
            System.out.println(" " + this.getName() + " has done a weak attack");
            p.setHp(p.getHp() - (this.strength/2));
            setStamina(stamina+1);
            if (p.getHp() <= 0){
                p.setAlive(false);
                p.setHp(0);
            }
        }

    }

}

