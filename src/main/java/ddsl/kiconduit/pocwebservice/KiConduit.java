package ddsl.kiconduit.pocwebservice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("kiconduit")
public class KiConduit {
	public KiConduit() {
	}
	
	@GET
	@Path("createGroup/{groupName}")
	@Produces("application/xml")
	public Response createGroup( @PathParam("groupName") String groupName) {
		int groupId = DataManager.createNewGroupId(groupName); 
		return Response.status(Status.OK).entity("<groupId>"+ groupId + "</groupId>").build();
	}

	
	@GET
	@Path("consulterGroup/{groupId}")
	@Produces("application/xml")
	public Response consulterGroup(@PathParam("groupId") int groupId) {
		KiConduitSynthese data = DataManager.getKiConduitSynthese(groupId); 
		//check groupId exists
		if(data==null){
			return Response.status(Status.CONFLICT).entity("<done>KO-No group found</done>").build();
		}
		
		Date date = data.getDate();
		String content="";
		List<KiConduitSessionUserData> userDatas = data.getUserDatas();
		for (KiConduitSessionUserData x : userDatas) {
			content+="<user name=\""+x.getName()+"\" nbConducteur=\""+x.getNbConducteur()+"\" nbConduit=\""+x.getNbConduit()+"\" nbLignes=\""+x.getNbLignes()+"\" />";
		}
		String baseContent="";
		KiConduitSynthese base = data.getBase();
		while(base!=null){
			Date date2 = base.getDate();
			baseContent+="<base date=\""+date2+"\" >";
			for (KiConduitSessionUserData y : base.getUserDatas()) {
				baseContent+="<user name=\""+y.getName()+"\" nbConducteur=\""+y.getNbConducteur()+"\" nbConduit=\""+y.getNbConduit()+"\" nbLignes=\""+y.getNbLignes()+"\" />";
			}
			baseContent+="</base>";
			base= base.getBase();
		}
		return Response.status(Status.OK).entity("<consulterGroupOK name=\""+data.getGroupName()+"\""+"><date>"+ date+"</date>" +content+ baseContent+"</consulterGroupOK>").build();
	}
	
	
	@GET
	@Path("addConducteur/{groupId}/{conducteurName}/{nbLignes}/{nbConducteur}/{nbConduit}")
	@Produces("application/xml")
	public Response addConducteur(@PathParam("groupId") int groupId, @PathParam("conducteurName") String conducteurName,@PathParam("nbLignes") int nbLignes,@PathParam("nbConducteur") int nbConducteur,@PathParam("nbConduit") int nbConduit) {
		
		KiConduitSynthese data = DataManager.getKiConduitSynthese(groupId); 
		//check groupId exists
		if(data==null){
			return Response.status(Status.CONFLICT).entity("<done>KO-No group found</done>").build();
		}
		//check that there is not already a same name  in this group
		Optional<KiConduitSessionUserData> findFirst = data.getUserDatas().stream().filter(x->x.getName().equals(conducteurName)).findFirst();
		if(findFirst.isPresent()){
			return Response.status(Status.CONFLICT).entity("<done>KO-User with such a name already exists in this group </done>").build();
		}
		data.getUserDatas().add(new KiConduitSessionUserData(conducteurName, nbLignes, nbConducteur, nbConduit));
		return Response.status(Status.OK).entity("<done>OK</done>").build();
	}
	
	@GET
	@Path("quiConduitLaProchaineFoisSiPresenceDe/{groupId}/{names}")
	@Produces("application/xml")
	public Response quiConduitLaProchaineFoisSiPresenceDe(@PathParam("groupId") int groupId,  @PathParam("names") String names) {
		String[] strings = names.split(":");
		KiConduitSynthese data = DataManager.getKiConduitSynthese(groupId); 
		String conducteur = data.quiConduitLaProchaineFoisSiPresenceDe(Arrays.asList(strings));
		return Response.status(Status.OK).entity("<conducteur>"+ conducteur + "</conducteur>").build();
	}

	@GET
	@Path("validerConducteur/{groupId}/{conduitsNames}/{conducteurName}/{yyyyMMddHHmm}")
	@Produces("application/xml")
	public Response validerConducteur(@PathParam("groupId") int groupId,  @PathParam("conduitsNames") String conduitsNames,  @PathParam("conducteurName") String conducteurName,@PathParam("yyyyMMddHHmm") String date) {
		KiConduitSynthese data = DataManager.getKiConduitSynthese(groupId); 
		//check groupId exists
		if(data==null){
			return Response.status(Status.CONFLICT).entity("<done>KO-No group found</done>").build();
		}
		String[] conduits = conduitsNames.split(":");
		Date ddate= null;
		try {
			ddate= new SimpleDateFormat("yyyyMMddHHmm").parse(date);
		} catch (ParseException e) {
			return Response.status(Status.CONFLICT).entity("<done>KO-UnknownDateFormat"+e.getMessage()+"(should be yyyyMMddHHmm)</done>").build();
		}
		DataManager.validerConducteur(groupId,data,conduits,conducteurName,ddate);  
		return Response.status(Status.OK).entity("<validerConducteurOK>"+ conducteurName + "</validerConducteurOK>").build();
	}	

}