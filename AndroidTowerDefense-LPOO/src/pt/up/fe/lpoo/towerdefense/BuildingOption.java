package pt.up.fe.lpoo.towerdefense;

import pt.up.fe.lpoo.framework.Image;

/**
 * Class containing building options for when the user selects a tile
 * @author Joao Marinheiro
 * @author Luis Cleto
 */
public class BuildingOption {
	/** width occupied by a building option icon in pixels */
	public static final int OPTION_ICON_WIDTH = 75; 
	private String optionCostStr;
	/** existing defense in the selected tile for building */
	public static DefenseObject selectedDefObj = null;
	
	/**
	 * Types of building options
	 */
	public enum OptionType {
		cancel, turretTower, boulderTower, barricade, tarPool, freezeTower, poisonTower, upgrade, destroy, repair
	}
	private OptionType option;
	
	/**
	 * Constructor for a building option with no cost
	 * @param opt building option type
	 */
	public BuildingOption(OptionType opt){
		this.option = opt;
		this.optionCostStr = ""; //$NON-NLS-1$
	}
	/**
	 * Constructor for a building option with a cost
	 * @param opt building option type
	 * @param optionCost cost of the building option
	 */
	public BuildingOption(OptionType opt, int optionCost){
		this.option = opt;
		this.optionCostStr = Integer.toString(optionCost);
	}

	public void setOptionCost(int cost){
		optionCostStr = Integer.toString(cost);
	}
	
	public OptionType getOptionType(){
		return option;
	}
	
	public Image getOptionIcon(){
		if(option == OptionType.cancel)
			return Assets.cancelIcon;
		else if(option == OptionType.upgrade)
			return Assets.upgradeIcon;
		else if(option == OptionType.turretTower)
			return Assets.turretTowerLevel1;
		else if(option == OptionType.barricade)
			return Assets.barricade;
		else if(option == OptionType.freezeTower)
			return Assets.freezeTower;
		else if(option == OptionType.poisonTower)
			return Assets.poisonTower;
		else if(option == OptionType.tarPool)
			return Assets.tarPool;
		else if(option == OptionType.boulderTower)
			return Assets.boulderTower;
		else if(option == OptionType.repair)
			return Assets.repairIcon;
		else
			return Assets.removeIcon;
	}
	
	public String getIconDescription(){
		if(option == OptionType.cancel)
			return Messages.getString("BuildingOption.1"); //$NON-NLS-1$
		else if(option == OptionType.destroy)
			return Messages.getString("BuildingOption.2"); //$NON-NLS-1$
		else if(option == OptionType.repair && selectedDefObj != null)
			return Integer.toString(selectedDefObj.getRepairCost());
		else
			return optionCostStr;
	}
}
