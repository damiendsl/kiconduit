Lancer KiConduitMain

// dabord creer un groupe
http://localhost:9991/rest/kiconduit/createGroup/les4DeBailleulsDecathlon

	=> on recupere le groupe id , 1 
//on een cree un autre histoire de verifier que le groupId s'incremente
http://localhost:9991/rest/kiconduit/createGroup/unAutrePourLePlaisir
	=> on recupere le groupe id  2 

=> consulter un groupe 
http://localhost:9991/rest/kiconduit/consulterGroup/1

// declarer 4  conducteurs qui n'ont jamais conduit sur le groupe 2  ) 
http://localhost:9991/rest/kiconduit/addConducteur/2/Tic/0/0/0
http://localhost:9991/rest/kiconduit/addConducteur/2/Tac/0/0/0
http://localhost:9991/rest/kiconduit/addConducteur/2/Toc/0/0/0
http://localhost:9991/rest/kiconduit/addConducteur/2/Tuc/0/0/0

// verifier qu'on ne peut pas declarer  un deuxieme  utilisateur de meme  nom pour un groupe  donné
http://localhost:9991/rest/kiconduit/addConducteur/2/Tac/0/0/0

// qui conduit la premier fois (  groupe 2  , mais Toc est malade ) 
localhost:9991/rest/kiconduit/quiConduitLaProchaineFoisSiPresenceDe/2/Tic:Tac:Tuc

	=> tic
confirmer la conduite de Tic
localhost:9991/rest/kiconduit/validerConducteur/2/Tac:Tuc/Toc/201710011200
// reconsulter  pour verifier l historique
http://localhost:9991/rest/kiconduit/consulterGroup/2