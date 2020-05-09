package com.noahcharlton.robogeddon.block.tileentity.fluid;

import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.message.MessageSerializer;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.XmlWriter;

public interface HasFluid {

    default void outputFluids(FluidBuffer output, TileEntity entity){
        var tile = entity.getRootTile();

        if(output.getAmount() < 1)
            return;

        float collectingFluids = 0;
        for(Tile neighbor : tile.getNeighbors()){
            if(neighbor.getTileEntity() instanceof HasFluid){
                var inputBuffer = ((HasFluid) neighbor.getTileEntity()).getInputBuffer();
                if(canTransferTo(output, inputBuffer))
                    collectingFluids++;
            }
        }

        if(collectingFluids == 0)
            return;

        float fluidPerConnection = 1 / collectingFluids;
        markDirty();
        for(Tile neighbor : tile.getNeighbors()){
            if(neighbor.getTileEntity() instanceof HasFluid){
                var inputBuffer = ((HasFluid) neighbor.getTileEntity()).getInputBuffer();

                if(canTransferTo(output, inputBuffer)){
                    var transferred = Math.min(fluidPerConnection, (output.getAmount() - inputBuffer.getAmount()) / 2f);
                    inputBuffer.acceptFluid(transferred);
                    output.retrieveFluid(transferred);
                    neighbor.getTileEntity().markDirty();

                    if(inputBuffer.getFluid() == null)
                        inputBuffer.setFluid(output.getFluid());
                }
            }
        }
    }

    private static boolean canTransferTo(FluidBuffer output, FluidBuffer inputBuffer) {
        return inputBuffer != null && hasCompatibleFluids(output, inputBuffer)
                && output.getAmount() - inputBuffer.getAmount() > 0 && !inputBuffer.isFull();
    }

    static boolean hasCompatibleFluids(FluidBuffer output, FluidBuffer inputBuffer) {
        return inputBuffer.getFluid() == null || output.getFluid().equals(inputBuffer.getFluid());
    }

    void markDirty();

    FluidBuffer getInputBuffer();

    FluidBuffer[] getFluidBuffers();

    void setFluidBuffers(FluidBuffer[] buffers);

    default String[] getFluidDetails(){
        var buffers = getFluidBuffers();
        var details = new String[buffers.length + 1];
        details[0] = "Fluids: \n";

        for(int i = 0; i < buffers.length; i++){
            var buffer = buffers[i];

            if(buffer.getFluid() != null){
                details[i + 1] = String.format("%s: %2.2f / %2.2f",
                        buffer.getFluid().getDisplayName(),
                        buffer.getAmount(),
                        buffer.getCapacity());
            }else{
                details[i + 1] = "No Fluid";
            }
        }

        return details;
    }

    static void save(HasFluid inventory, XmlWriter xml) {
        var element = xml.element("Fluids");

        for(FluidBuffer buffer : inventory.getFluidBuffers()){
            element.element("Buffer", MessageSerializer.toJson(buffer, FluidBuffer.class));
        }

        element.pop();
    }

    static void load(HasFluid inventory, Element xml) {
        var element = xml.getChildByName("Fluids");
        var buffers = new FluidBuffer[element.getChildCount()];

        for(int i = 0; i < buffers.length; i++) {
            buffers[i] = MessageSerializer.fromJson(element.getChild(i).getText(), FluidBuffer.class);
        }

        inventory.setFluidBuffers(buffers);
    }
}
