package noppes.npcs.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.scripted.NpcAPI;
import noppes.npcs.scripted.interfaces.IItemStack;
import noppes.npcs.scripted.item.ScriptCustomItem;
import org.lwjgl.opengl.GL11;

public class ItemScripted extends Item implements ItemRenderInterface {
    public ItemScripted() {
        maxStackSize = 1;
        setCreativeTab(CustomItems.tab);
        CustomNpcs.proxy.registerItem(this);
        setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister){
        this.itemIcon = Items.iron_pickaxe.getIconFromDamage(0);
    }

    @Override
    public int getColorFromItemStack(ItemStack itemStack, int par2){
        return 0x8B4513;
    }

    @Override
    public boolean requiresMultipleRenderPasses(){
        return true;
    }

    @Override
    public Item setUnlocalizedName(String name){
        GameRegistry.registerItem(this, name);
        return super.setUnlocalizedName(name);
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer entityPlayer)
    {
        if(!world.isRemote)
            return stack;

        if(entityPlayer.isSneaking()) {
            CustomNpcs.proxy.openGui(0, 0, 0, EnumGuiType.ScriptItem, entityPlayer);
        }
        entityPlayer.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        return stack;
    }

    public int getMaxItemUseDuration(ItemStack stack){
        return (new ScriptCustomItem(stack)).getMaxItemUseDuration();
    }

    public static ScriptCustomItem GetWrapper(ItemStack stack) {
        return new ScriptCustomItem(stack);
    }

    public boolean showDurabilityBar(ItemStack stack) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof ScriptCustomItem && (new ScriptCustomItem(stack)).durabilityShow;
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof ScriptCustomItem ? 1.0D - (new ScriptCustomItem(stack)).durabilityValue : 1.0D;
    }

    public int getItemStackLimit(ItemStack stack) {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof ScriptCustomItem ? (new ScriptCustomItem(stack)).getMaxStackSize() : super.getItemStackLimit(stack);
    }

    public boolean isItemTool(ItemStack stack)
    {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof ScriptCustomItem ? (new ScriptCustomItem(stack)).isTool() : super.isItemTool(stack);
    }

    public float getDigSpeed(ItemStack stack, Block block, int metadata)
    {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof ScriptCustomItem ? (new ScriptCustomItem(stack)).getDigSpeed() : super.getDigSpeed(stack, block, metadata);
    }

    public boolean isValidArmor(ItemStack stack, int armorType, Entity entity)
    {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);

        if((new ScriptCustomItem(stack)).getArmorType() == -1)
            return true;

        return istack instanceof ScriptCustomItem ? armorType == (new ScriptCustomItem(stack)).getArmorType() : super.isValidArmor(stack, armorType, entity);
    }

    public int getItemEnchantability(ItemStack stack)
    {
        IItemStack istack = NpcAPI.Instance().getIItemStack(stack);
        return istack instanceof ScriptCustomItem ? (new ScriptCustomItem(stack)).getEnchantability() : super.getItemEnchantability(stack);
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return true;
    }

    @Override
    public void renderSpecial() {}

    public void renderCustomAttributes(ItemStack itemStack) {
        ScriptCustomItem scriptCustomItem = GetWrapper(itemStack);

        GL11.glTranslatef(scriptCustomItem.translateX, scriptCustomItem.translateY, scriptCustomItem.translateZ);
        GL11.glRotatef(scriptCustomItem.rotationX, 1, 0, 0);
        GL11.glRotatef(scriptCustomItem.rotationY, 0, 1, 0);
        GL11.glRotatef(scriptCustomItem.rotationZ, 0, 0, 1);
        GL11.glScalef(scriptCustomItem.scaleX, scriptCustomItem.scaleY, scriptCustomItem.scaleZ);

        int color = scriptCustomItem.getColor();
        float itemRed = (color >> 16 & 255) / 255f;
        float itemGreen = (color >> 8  & 255) / 255f;
        float itemBlue = (color & 255) / 255f;
        GL11.glColor4f(itemRed, itemGreen, itemBlue, 1.0F);
    }
}
