public class Wizard extends Character{
    private int mana;
    private int intelligence;

    public Wizard(String name, int hp, int mana, int intelligence) {
        super(name, hp);
        this.mana = mana;
        this.intelligence = intelligence;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    @Override
    public void attack(Character p) {
        //fireball attack
        System.out.print("With mana " + mana + " and intelligence " + intelligence);
        if(mana >= 5){
            System.out.println(" " + getName()  + " casted a fireball spell");
            p.setHp(getHp() - intelligence);
            setMana(mana - 5);
        // staff hit
        } else {
            System.out.println(" " + getName() + " has done a staff hit attack");
            p.setHp(p.getHp() - 2);
            mana++;
        }

        if(p.getHp() <= 0){
            p.setAlive(false);
            p.setHp(0);
        }

    }

}
