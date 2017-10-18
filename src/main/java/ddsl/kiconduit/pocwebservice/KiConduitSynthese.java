package ddsl.kiconduit.pocwebservice;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Aggrgagtion des données permattant de calculer qui doit conduire
 * 
 * @author a185735
 *
 */
public class KiConduitSynthese {

	//////////////////////////////
	//////// propriétés
	//////////////////////////////

	private String groupName;
	private Date date;
	private KiConduitSynthese base; // null if this = chaine origin
	private List<KiConduitSessionUserData> userDatas;

	//////////////////////////////
	//////// Constructeur
	//////////////////////////////

	public KiConduitSynthese(String groupName) {
		this.groupName = groupName;
	}

	//////////////////////////////
	//////// getters and setters
	//////////////////////////////

	public KiConduitSynthese getBase() {
		return base;
	}

	public void setBase(KiConduitSynthese base) {
		this.base = base;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<KiConduitSessionUserData> getUserDatas() {
		if(userDatas==null){
			userDatas= new ArrayList<>();
		}
		return userDatas;
	}

	public void setUserDatas(List<KiConduitSessionUserData> userDatas) {
		this.userDatas = userDatas;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	//////////////////////////////
	//////// metier
	//////////////////////////////	

	/**
	 * 
	 * @param names
	 *            liste des noms des personnes presentes
	 * @return la personne qui conduit parmi les personnes presentes . La régle
	 *         de calcul est : si une personne n'a jamais conduit , on prend la
	 *         premiere trouvé filtrer parmi names et ne retenir que ceux qui
	 *         ont une dette nette de conduire appeler filterThoseWithNetDebt
	 *         s'il y en a un seul c'est celui la s'il y en a plus d' un alors
	 *         parmi ceux restants , appeler computeFor si il n'y a aucun qui a
	 *         une dette nette de conduire, suivre la meme logique de calcul (
	 *         appeler methode computeFor ) que precedemment mais sans ce filtre
	 * 
	 */
	public String quiConduitLaProchaineFoisSiPresenceDe(List<String> names) {
		if (names == null || names.size() == 1) {
			return null;
		}
		Optional<KiConduitSessionUserData> optional = userDatas.stream().filter(x -> names.contains(x.getName()))
				.filter(x -> x.getNbLignes() == 0).findFirst();
		if (optional.isPresent()) {
			return optional.get().getName();
		}
		// on a au moins deux personnes
		List<String> namesHavingDebt = filterThoseWithNetDebt(names);
		if (namesHavingDebt.isEmpty()) {
			return computeFor(names, names.size());
		} else if (namesHavingDebt.size() == 1) {
			return namesHavingDebt.get(0);
		} else {
			return computeFor(namesHavingDebt, names.size());
		}
	}

	/**
	 * 
	 * @param names
	 *            liste des noms des personnes presentes
	 * @return la personne qui conduit parmi les personnes presentes . La régle
	 *         de calcul est : retenir les 2 extremes et calculer leur moyenne
	 *         nbPersonnesConduites(user) respectives calculer la moyenneGlobale
	 *         du nombre de passagers conduits ( peu importe la liste des
	 *         personnes presentes ) -> globalNbPersonnesConduites , environ 2
	 *         normalement .. prendre celui qui est a la plus grande distance de
	 *         la moyenne
	 * 
	 */
	private String computeFor(List<String> names, int nbConduitLa) {
		final Comparator<KiConduitSessionUserData> comparatorConduitRatio = (p1, p2) -> Double.compare(
				((double) p1.getNbConduit()) / p1.getNbLignes(), ((double) p2.getNbConduit()) / p2.getNbLignes());
		KiConduitSessionUserData min = userDatas.stream().filter(x -> names.contains(x.getName())).min(comparatorConduitRatio).get();
		KiConduitSessionUserData max = userDatas.stream().filter(x -> names.contains(x.getName())).max(comparatorConduitRatio).get();
		double dMin = ((double) min.getNbConducteur()) / min.getNbLignes();
		double dMax = ((double) max.getNbConducteur()) / max.getNbLignes();
		double distanceMin = Math.abs(nbConduitLa - dMin);
		double distanceMax = Math.abs(nbConduitLa - dMax);
		if (distanceMin < distanceMax) {
			return max.getName();
		} else {
			return min.getName();
		}

	}

	/**
	 * Filtrer les utilisateurs pour ne retenir que ceux qui ont nbConducteur <
	 * nbConducteur
	 * 
	 * @param names
	 * @return
	 */
	private List<String> filterThoseWithNetDebt(List<String> names) {
		return userDatas.stream().filter(x -> x.getNbConducteur() < x.getNbConduit()).map(KiConduitSessionUserData::getName).collect(Collectors.toList());
	}

}
