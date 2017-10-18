package ddsl.kiconduit.pocwebservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DataManager {

	/**
	 * Map with groupId as key
	 */
	public static Map<Integer, KiConduitSynthese> datas= new HashMap<>();

	/**
	 * need to know your group id , if you have not a group yet , create  one  using createNewGroupId
	 * @param groupId
	 * @return
	 */
	public static KiConduitSynthese getKiConduitSynthese(int groupId) {
		return datas.get(groupId);
	}
	public static int createNewGroupId(String groupName) {
		 Optional<Integer> newIdOptional =  datas.keySet().stream().sorted().reduce((first, second) -> second) ;
		 int newId = 1;
		 if(newIdOptional.isPresent()){
			 newId = newIdOptional.get() + 1;
		 }
		 KiConduitSynthese x =  new KiConduitSynthese(groupName);
		 x.setDate(new Date());
		 datas.put(newId, x);
		 return newId;
	}


//	static {
//		datas= new HashMap<>();
//		KiConduitSynthese value = new KiConduitSynthese("DecatBailleulTeam4");
//		List<KiConduitSessionUserData> userDatas = new ArrayList<>();
//		userDatas.add(new KiConduitSessionUserData("Benoit", 41, 85, 84));
//		userDatas.add(new KiConduitSessionUserData("Thomas", 42, 82, 84));
//		userDatas.add(new KiConduitSessionUserData("Damien", 45, 94, 95));
//		userDatas.add(new KiConduitSessionUserData("Manu", 45, 92, 90));
//		value.setUserDatas(userDatas);
//		datas.put(11, value);
//	}


	public static void validerConducteur(int groupId, KiConduitSynthese base, String[] conduits, String conducteurName,
			Date date) {
		
		// creer une nouvelle ligne KiConduitSynthese  qui pointe  vers l'ancienne 
		KiConduitSynthese newKiConduitSynthese = new KiConduitSynthese(base.getGroupName());
		newKiConduitSynthese.setDate(date);
		newKiConduitSynthese.setBase(base);
		List<KiConduitSessionUserData> userDatas = base.getUserDatas();
		List<KiConduitSessionUserData> newList =  new ArrayList<>();
		for (KiConduitSessionUserData y : userDatas) {
			KiConduitSessionUserData clone = new KiConduitSessionUserData(y.getName(), y.getNbLignes(), y.getNbConducteur(), y.getNbConduit());
			if(clone.getName().equals(conducteurName)){
				clone.setNbLignes(clone.getNbLignes()+1);
				clone.setNbConducteur(clone.getNbConducteur()+conduits.length);
			}
			for (String conduitName : conduits) {
				if(clone.getName().equals(conduitName)){
					clone.setNbConduit(clone.getNbConduit() + 1 );
				}
			}
			newList.add(clone);
		}
		newKiConduitSynthese.setUserDatas(newList);
		
		// sauvegarder la nouvelle ligne  pour ce groupId
		datas.put(groupId, newKiConduitSynthese);
	}



}
