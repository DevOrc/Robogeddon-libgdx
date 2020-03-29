package com.noahcharlton.robogeddon.block;

import com.noahcharlton.robogeddon.world.item.ItemStack;
import com.noahcharlton.robogeddon.world.item.Items;

public class OilBlock extends Block implements Mineable{

    public OilBlock(String id) {
        super(id);
    }

    @Override
    public ItemStack onMine() {
        return Items.rock.stack(3);
    }
}
