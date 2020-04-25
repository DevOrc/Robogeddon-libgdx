package com.noahcharlton.robogeddon.world.item;

public class ItemStack {

    private Item item;
    private int amount;

    public ItemStack(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public String getDisplayInfo(){
        return amount + " " + item.getDisplayName();
    }

    @Override
    public String toString() {
        return "ItemStack{" +
                "item=" + item +
                ", amount=" + amount +
                '}';
    }
}
