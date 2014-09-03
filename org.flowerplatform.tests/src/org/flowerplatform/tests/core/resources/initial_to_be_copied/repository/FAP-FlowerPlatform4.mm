<map version="freeplane 1.2.0">
<!--To view this file, download free mind mapping software Freeplane from http://freeplane.sourceforge.net -->
<node STYLE_REF="In progress (Yellow)" FOLDED="false" ID="ID_85319927" CREATED="1325603285827" MODIFIED="1392387817129" STYLE="bubble"><richcontent TYPE="NODE">

<html>
  <head>
    
  </head>
  <body>
    <p style="text-align: center">
      Flower Platform 4
    </p>
    <p style="text-align: center">
      <i>- Flower Agile Process Sheet -</i>
    </p>
  </body>
</html>
</richcontent>
<hook NAME="MapStyle">
    <properties show_icon_for_attributes="true" show_note_icons="true" show_notes_in_map="false"/>

<map_styles>
<stylenode LOCALIZED_TEXT="styles.root_node">
<stylenode LOCALIZED_TEXT="styles.predefined" POSITION="right">
<stylenode LOCALIZED_TEXT="default" MAX_WIDTH="600" COLOR="#000000" STYLE="as_parent">
<font NAME="SansSerif" SIZE="9" BOLD="false" ITALIC="false"/>
</stylenode>
<stylenode LOCALIZED_TEXT="defaultstyle.details"/>
<stylenode LOCALIZED_TEXT="defaultstyle.note" STYLE="fork">
<cloud COLOR="#ffff99" SHAPE="ARC"/>
</stylenode>
<stylenode LOCALIZED_TEXT="defaultstyle.floating">
<edge STYLE="hide_edge"/>
<cloud COLOR="#f0f0f0" SHAPE="ROUND_RECT"/>
</stylenode>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.user-defined" POSITION="right">
<stylenode TEXT="Features node (Blue cloud)">
<cloud COLOR="#e9e9ff" SHAPE="ROUND_RECT"/>
<edge STYLE="bezier"/>
</stylenode>
<stylenode TEXT="Roadmap node (Light yellow cloud)">
<cloud COLOR="#ffffe2" SHAPE="ROUND_RECT"/>
</stylenode>
<stylenode TEXT="Code" COLOR="#ffffff" BACKGROUND_COLOR="#6666ff" STYLE="bubble">
<font NAME="Courier New" SIZE="14" BOLD="true" ITALIC="true"/>
</stylenode>
<stylenode TEXT="New (Red)" BACKGROUND_COLOR="#ff5858"/>
<stylenode TEXT="Planned (Orange)" BACKGROUND_COLOR="#ffcc00"/>
<stylenode TEXT="In progress (Yellow)" BACKGROUND_COLOR="#ffff00"/>
<stylenode TEXT="Done (Blue)" BACKGROUND_COLOR="#9999ff"/>
<stylenode TEXT="Resubmitted (Violet)" BACKGROUND_COLOR="#ff00ff"/>
<stylenode TEXT="In progress 2 (Light Yellow)" BACKGROUND_COLOR="#ffffcc"/>
<stylenode TEXT="Done 2 (Light Blue)" BACKGROUND_COLOR="#cccdfc"/>
<stylenode TEXT="Validated (Green)" BACKGROUND_COLOR="#33ff33"/>
<stylenode LOCALIZED_TEXT="styles.topic" COLOR="#18898b" STYLE="fork">
<font NAME="Liberation Sans" SIZE="12" BOLD="true"/>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.subtopic" COLOR="#cc3300" STYLE="fork">
<font NAME="Liberation Sans" SIZE="12" BOLD="true"/>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.subsubtopic" COLOR="#669900">
<font NAME="Liberation Sans" SIZE="12" BOLD="true"/>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.important">
<icon BUILTIN="yes"/>
</stylenode>
</stylenode>
<stylenode LOCALIZED_TEXT="styles.AutomaticLayout" POSITION="right">
<stylenode LOCALIZED_TEXT="AutomaticLayout.level.root" COLOR="#000000">
<font SIZE="20"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,1" COLOR="#0033ff">
<font SIZE="18"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,2" COLOR="#00b439">
<font SIZE="16"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,3" COLOR="#990000">
<font SIZE="14"/>
</stylenode>
<stylenode LOCALIZED_TEXT="AutomaticLayout.level,4" COLOR="#111111">
<font SIZE="12"/>
</stylenode>
</stylenode>
</stylenode>
</map_styles>
</hook>
<node TEXT="_Admin" POSITION="right" ID="ID_1242916093" CREATED="1389699125393" MODIFIED="1389699127164"/>
<node TEXT="_Investigations" FOLDED="true" POSITION="right" ID="ID_1405228053" CREATED="1389699132583" MODIFIED="1389699137154">
<node TEXT="Closed" FOLDED="true" ID="ID_1701340580" CREATED="1390463480250" MODIFIED="1390463613683">
<icon BUILTIN="flag-green"/>
<node TEXT="GH3 - Prototype: Mindmap editor, with old (FP2/3) infrastructure" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1947973484" CREATED="1389788036618" MODIFIED="1390463609667" LINK="https://github.com/flower-platform/flower-platform-4/issues/3">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_1344069889" CREATED="1389788128040" MODIFIED="1389788128040">
<node TEXT="RM8820 - GH3 - Prototype: Mindmap editor, with old (FP2/3) infrastructure" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1592818889" CREATED="1389788128040" MODIFIED="1389788128050" LINK="http://csp1/redmine/issues/8820"/>
<node TEXT="RM8821 - [Review] GH3 - Prototype: Mindmap editor, with old (FP2/3) infrastructure" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1792599307" CREATED="1389788128270" MODIFIED="1389788128280" LINK="http://csp1/redmine/issues/8821"/>
</node>
<node TEXT="Expected results" ID="ID_1654575654" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="Prototype:" ID="ID_1530915313" CREATED="1389384571076" MODIFIED="1389384988300"/>
<node TEXT="Server" ID="ID_114295142" CREATED="1389360453943" MODIFIED="1389360455293">
<node TEXT="Node" ID="ID_1839443771" CREATED="1389360437628" MODIFIED="1389360444295"/>
<node TEXT="NodeDao" ID="ID_1419150567" CREATED="1389360440179" MODIFIED="1389360447559"/>
</node>
<node TEXT="Client" ID="ID_631004327" CREATED="1389360458724" MODIFIED="1389360462845">
<node TEXT="facut un view nou (de tip editor)" ID="ID_582422725" CREATED="1389360534383" MODIFIED="1389360545828"/>
<node TEXT="care contine un diagram renderer, cu implementarea existenta de mindmap" ID="ID_1689737740" CREATED="1389360538264" MODIFIED="1389360568230"/>
<node TEXT="modificata pentru noul api" ID="ID_1261648860" CREATED="1389360568429" MODIFIED="1389360572530"/>
<node TEXT="fara mecanism de update: avem un buton refresh; la apasare face refresh la toate" ID="ID_1153114021" CREATED="1389360575647" MODIFIED="1389360589234"/>
<node TEXT="buton de reload from disk: interop cu freeplane" ID="ID_610366966" CREATED="1389360683344" MODIFIED="1389360694964"/>
</node>
</node>
<node TEXT="Construction site" FOLDED="true" ID="ID_208759306" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="Editor" ID="ID_1716344471" CREATED="1389272617405" MODIFIED="1389272636717">
<node TEXT="metoda de obtinere de copiii, pentru un nod cu un anumit id unic" ID="ID_1228943760" CREATED="1389272637765" MODIFIED="1389272663627"/>
<node TEXT="sistemul de update la ceilalti vieweri? Cum bazam pe polling?" ID="ID_1149382122" CREATED="1389272756056" MODIFIED="1389273254278">
<icon BUILTIN="smily_bad"/>
<node TEXT="Sper sa avem acces imediat dupa id." ID="ID_1283652302" CREATED="1389272897055" MODIFIED="1389272930917"/>
<node TEXT="Avem o lista de timestamp-uri de modificare." ID="ID_1982207346" CREATED="1389272931245" MODIFIED="1389272940117"/>
<node TEXT="Clientul intreaba periodic (e.g. la 5 secunde): Am modificari pentru nodurile: id8, id10, id78? Astfel serverul poate sa ii raspunda exact si cu modificarile, sau trimite direct nodul" ID="ID_1228006152" CREATED="1389272940295" MODIFIED="1389272984707"/>
<node TEXT="Am putea sa avem un mecanism, prin care intrebam doar despre nodurile care sunt la ecran" ID="ID_1991262128" CREATED="1389272998425" MODIFIED="1389273023657"/>
<node TEXT="Am putea sa impartim pe pagini?" ID="ID_1661211281" CREATED="1389273053605" MODIFIED="1389273060877"/>
</node>
<node TEXT="TODO" ID="ID_942638792" CREATED="1389277568083" MODIFIED="1389277572456">
<node TEXT="de adaugat pe master branch-urile modif dupa review (in special cele legate de tool-uri, toolbar)" ID="ID_119186859" CREATED="1389277573412" MODIFIED="1389278029492"/>
<node TEXT="acum, in selectie, putem avea si Diagram -&gt; de modif actiunile (majoritatea crapa)" ID="ID_1939083249" CREATED="1389277753114" MODIFIED="1389277819093"/>
<node TEXT="update-urile de pe server nu ajung pe client" ID="ID_1041494273" CREATED="1389277867481" MODIFIED="1389277927765"/>
<node TEXT="ar trebui sa pastram grid-ul in spate?" ID="ID_351208397" CREATED="1389277948256" MODIFIED="1389277957342"/>
<node TEXT="side = RIGHT by default ???" ID="ID_1033856624" CREATED="1389344358654" MODIFIED="1389344377585"/>
<node TEXT="properties" ID="ID_1227341965" CREATED="1389345989474" MODIFIED="1389346116636">
<node TEXT="name" ID="ID_304952550" CREATED="1389346122015" MODIFIED="1389346126246"/>
<node TEXT="icons" ID="ID_383986342" CREATED="1389346126774" MODIFIED="1389346128736"/>
<node TEXT="side" ID="ID_1673093940" CREATED="1389346129204" MODIFIED="1389346136096"/>
<node TEXT="minWidth &amp; maxWidth" ID="ID_754588225" CREATED="1389346136454" MODIFIED="1389346158826"/>
<node TEXT="expanded" ID="ID_1181067168" CREATED="1389346176136" MODIFIED="1389346178346"/>
</node>
</node>
</node>
<node TEXT="Freeplane Persistence" ID="ID_393210392" CREATED="1389344386934" MODIFIED="1389344393135">
<node TEXT="model" ID="ID_523615795" CREATED="1389345760829" MODIFIED="1389345762511">
<node TEXT="NodeModel" ID="ID_1845795400" CREATED="1389345694840" MODIFIED="1389345697631">
<node TEXT="parent" ID="ID_1188438823" CREATED="1389345698449" MODIFIED="1389345700891"/>
<node TEXT="children" ID="ID_82257803" CREATED="1389345701369" MODIFIED="1389345703221"/>
</node>
<node TEXT="MapModel" ID="ID_1870485005" CREATED="1389345446438" MODIFIED="1389345658421">
<node TEXT="root:NodeModel" ID="ID_1627323927" CREATED="1389345659369" MODIFIED="1389345692551"/>
<node TEXT="nodes: Map&lt;String, NodeModel&gt;" ID="ID_868941097" CREATED="1389345667170" MODIFIED="1389345684621"/>
</node>
</node>
<node TEXT="dao" ID="ID_885044487" CREATED="1389198064756" MODIFIED="1389198067658">
<node TEXT="getNode(id):Node" LOCALIZED_STYLE_REF="default" ID="ID_1115217307" CREATED="1389198068686" MODIFIED="1389344473376">
<node TEXT="MapController.getNodeFromID(id)" ID="ID_440293218" CREATED="1389345303739" MODIFIED="1389345314290"/>
</node>
<node TEXT="getChildren(id):List" LOCALIZED_STYLE_REF="default" ID="ID_1751474671" CREATED="1389198082246" MODIFIED="1389344473376">
<node TEXT="NodeModel.getChildren()" ID="ID_1793229211" CREATED="1389345124368" MODIFIED="1389345130279"/>
</node>
<node TEXT="getChild(id, key):Node" LOCALIZED_STYLE_REF="default" ID="ID_66550257" CREATED="1389198132706" MODIFIED="1389344473376">
<node TEXT="NodeModel.getChildAt()" ID="ID_1444740353" CREATED="1389345132007" MODIFIED="1389345150989"/>
</node>
<node TEXT="getParent(id):Node" LOCALIZED_STYLE_REF="default" FOLDED="true" ID="ID_1038231339" CREATED="1389198107407" MODIFIED="1389344473386">
<node TEXT="NodeModel.getParent()" ID="ID_449401428" CREATED="1389345108247" MODIFIED="1389345114699"/>
</node>
<node TEXT="getLinks(id, category)" LOCALIZED_STYLE_REF="default" ID="ID_367487168" CREATED="1389198187777" MODIFIED="1389374990637">
<attribute_layout NAME_WIDTH="59" VALUE_WIDTH="130"/>
</node>
<node TEXT="addNode(parentId, type)" LOCALIZED_STYLE_REF="default" ID="ID_331614991" CREATED="1389198661367" MODIFIED="1389374996407">
<node TEXT="-&gt; MMapController.addNewNode(parent, index, left/right)" ID="ID_951765155" CREATED="1389344671776" MODIFIED="1389344826427"/>
</node>
<node TEXT="getPropertyValue(propertyName):Object" LOCALIZED_STYLE_REF="default" ID="ID_394333757" CREATED="1389199879001" MODIFIED="1389375046737"><richcontent TYPE="DETAILS" HIDDEN="true">

<html>
  <head>
    
  </head>
  <body>
    <p>
      test
    </p>
  </body>
</html>
</richcontent>
<node TEXT="NodeModel.getAttributes().getFirst(name)" ID="ID_256307352" CREATED="1389345386518" MODIFIED="1389345427170"/>
</node>
</node>
<node TEXT="save -&gt; MFileManager.save(MapModel)" ID="ID_716444653" CREATED="1389345728159" MODIFIED="1389345743221"/>
</node>
<node TEXT="Posibilitate afisare mai multe view-uri ale MM. Poate chiar si un view sa consideram root un anumit nod." ID="ID_1207573014" CREATED="1389354974772" MODIFIED="1389355012395"/>
<node TEXT="intrebari" STYLE_REF="New (Red)" ID="ID_1621446252" CREATED="1389356962968" MODIFIED="1389356973474">
<icon BUILTIN="messagebox_warning"/>
<node TEXT="cum facem cu side?" ID="ID_1644141715" CREATED="1389356965505" MODIFIED="1389356968406">
<node TEXT="Lasam userul sa-si defineasca side-ul?" ID="ID_1314071150" CREATED="1389356984473" MODIFIED="1389356992252"/>
</node>
<node TEXT="sa tinem in sesiune id-uri de noduri deschise de acel client?" ID="ID_346463190" CREATED="1389360014695" MODIFIED="1389360041436">
<node TEXT="ar ajuta cand vreau sa trimit initiatorului" ID="ID_1561457107" CREATED="1389360081208" MODIFIED="1389360089957"/>
<node TEXT="nu m-ar ajuta sa fac push in timp real pt toti userii, pentru ca nu am si o mapare inversa: nod -&gt; lista de vieweri. Pentru ca info din sesiune sunt separate." ID="ID_353708417" CREATED="1389360090186" MODIFIED="1389360129823"/>
<node TEXT="ar ajuta totusi poate, atunci cand userul intreaba de ce s-a modificat: nu mai trimite si droaia de noduri deschise la fiecare 5 secunde." ID="ID_325591760" CREATED="1389360130116" MODIFIED="1389360163675"/>
</node>
</node>
<node TEXT="pentru un anumit tip de nod" ID="ID_1414309535" CREATED="1389357325879" MODIFIED="1389357345317">
<node TEXT="server" ID="ID_1800481825" CREATED="1389357520479" MODIFIED="1389357521540">
<node TEXT="properties provider" ID="ID_316376141" CREATED="1389357347479" MODIFIED="1389357361749">
<node TEXT="ne spune care sunt numele de proprietati" ID="ID_1065091691" CREATED="1389357362606" MODIFIED="1389357376609"/>
<node TEXT="nu pare sa fie nevoie sa ne ofere si o cale de a scrie/citi in ele. Asta ne e oferit by default de catre dao de noduri" STYLE_REF="Resubmitted (Violet)" ID="ID_648130531" CREATED="1389357458700" MODIFIED="1389357494171">
<icon BUILTIN="messagebox_warning"/>
</node>
</node>
<node TEXT="un serviciu cu metode de api. Adica operatiuni posibile" ID="ID_1478377254" CREATED="1389357504384" MODIFIED="1389357520257">
<node TEXT="e.g." STYLE_REF="Done (Blue)" ID="ID_1611276746" CREATED="1389357706371" MODIFIED="1389357716400"/>
<node TEXT="codeSync" ID="ID_304946215" CREATED="1389357718955" MODIFIED="1389357724704">
<node TEXT="Synchronize" ID="ID_980771228" CREATED="1389357725705" MODIFIED="1389357730713"/>
<node TEXT="Revert" ID="ID_1909203704" CREATED="1389357730974" MODIFIED="1389357732761"/>
</node>
</node>
<node TEXT="optional: interceptie operatiuni edtior" ID="ID_1516537646" CREATED="1389357836138" MODIFIED="1389357845275">
<node TEXT="e.g." STYLE_REF="Done (Blue)" ID="ID_488506856" CREATED="1389357965642" MODIFIED="1389358369212"/>
<node TEXT="codeSync" ID="ID_285119026" CREATED="1389357967894" MODIFIED="1389357972385">
<node TEXT="modificare propr syncronizabila =&gt; creare prop.originalValue + update sync &amp; childrenSync pe ierarhie. Actualizeaza si titlul" ID="ID_1522016185" CREATED="1389357973273" MODIFIED="1389359336084"/>
</node>
</node>
<node TEXT="server/stocare ca fisier" ID="ID_1114319899" CREATED="1389358306118" MODIFIED="1389358321515">
<node TEXT="logica speciala de persistare" ID="ID_398285109" CREATED="1389358360134" MODIFIED="1389358373243">
<node TEXT="e.g." STYLE_REF="Done (Blue)" ID="ID_1522118883" CREATED="1389358374069" MODIFIED="1389358469333"/>
<node TEXT="repository" ID="ID_1058494846" CREATED="1389358375520" MODIFIED="1389358379296">
<node TEXT="duce catre un director gen fp-data" ID="ID_1378827326" CREATED="1389358390072" MODIFIED="1389358403781"/>
</node>
<node TEXT="nodurile de tip mindMapFile" ID="ID_1871905621" CREATED="1389358405157" MODIFIED="1389358423659">
<node TEXT="duc catre un fisier" ID="ID_1056160522" CREATED="1389358414074" MODIFIED="1389358420760"/>
</node>
<node TEXT="nodurile de tip mindMapNode" ID="ID_792884747" CREATED="1389358423960" MODIFIED="1389358430128">
<node TEXT="anumite proprietati sunt stocate &quot;nativ&quot;. E.g. culoare, font, stiluri" ID="ID_1847357607" CREATED="1389358431120" MODIFIED="1389358449460"/>
</node>
</node>
</node>
</node>
<node TEXT="client" ID="ID_42030148" CREATED="1389357525056" MODIFIED="1389357527036">
<node TEXT="actiuni" ID="ID_1603066302" CREATED="1389357528527" MODIFIED="1389358273192">
<node TEXT="enablement" ID="ID_1663795073" CREATED="1389357602300" MODIFIED="1389357603857">
<node TEXT="Exista posibilitatea sa se execute un enablement client side, pt ca avem acces la proprietati" ID="ID_220126797" CREATED="1389357600240" MODIFIED="1389357636705"/>
<node TEXT="exista totusi limitarea: daca actiunea are nevoie de propr care apartin de noduri care nu sunt vizibile" ID="ID_1881498331" CREATED="1389357647391" MODIFIED="1389357663778"/>
</node>
<node TEXT="in principiu apeleaza metodele de api/serviciu" ID="ID_528806428" CREATED="1389357667798" MODIFIED="1389357741072"/>
</node>
<node TEXT="renderer" ID="ID_1697262378" CREATED="1389358269806" MODIFIED="1389358271013"/>
</node>
</node>
<node TEXT="platforma trebuie sa ofere" ID="ID_1032008727" CREATED="1389357751077" MODIFIED="1389357758024">
<node TEXT="stocare de proprietati pe nod. Map&lt;String, String&gt;" ID="ID_1363193343" CREATED="1389357758897" MODIFIED="1389357771458"/>
<node TEXT="operatiuni" ID="ID_1618962567" CREATED="1389357802644" MODIFIED="1389357804581">
<node TEXT="add" ID="ID_1816825027" CREATED="1389357805415" MODIFIED="1389357806954"/>
<node TEXT="remove" ID="ID_254858758" CREATED="1389357807170" MODIFIED="1389357808466"/>
<node TEXT="move" ID="ID_240999673" CREATED="1389357808681" MODIFIED="1389357810219"/>
<node TEXT="rename" ID="ID_1636734770" CREATED="1389357810665" MODIFIED="1389357814060"/>
<node TEXT="change prop" ID="ID_1752166887" CREATED="1389357814409" MODIFIED="1389357817415"/>
<node TEXT="get prop" ID="ID_1721599962" CREATED="1389357817637" MODIFIED="1389357821791"/>
</node>
<node TEXT="iterceptori pentru actiuni" ID="ID_501650723" CREATED="1389357824253" MODIFIED="1389357831065"/>
<node TEXT="partea de editare" ID="ID_1121218515" CREATED="1389359467571" MODIFIED="1389359470819">
<node TEXT="Cand editez =&gt; apelez serviciu. Fie cel standard, fie servicii &quot;custom&quot;, probabil prin intermediul proprietatilor" ID="ID_1031567213" CREATED="1389359488657" MODIFIED="1389359518372"/>
<node TEXT="Trebuie" ID="ID_18434936" CREATED="1389359525476" MODIFIED="1389359527680">
<node TEXT="eu sa primesc update-uri" ID="ID_862226294" CREATED="1389359528522" MODIFIED="1389359532411">
<node TEXT="nod adaugat" ID="ID_493703650" CREATED="1389359691638" MODIFIED="1389359706070"/>
<node TEXT="nod sters" ID="ID_389795878" CREATED="1389359719205" MODIFIED="1389359723004"/>
<node TEXT="titlu modificat" ID="ID_1405548949" CREATED="1389359726259" MODIFIED="1389359731749"/>
<node TEXT="proprietate modificata" ID="ID_1614661261" CREATED="1389359731964" MODIFIED="1389359738724"/>
</node>
<node TEXT="altii sa primeasca update-uri" ID="ID_1112300426" CREATED="1389359532633" MODIFIED="1389359539016"/>
</node>
</node>
</node>
<node TEXT="api rest" ID="ID_484670844" CREATED="1389355037259" MODIFIED="1389355043664">
<node TEXT="necesitati" ID="ID_510145717" CREATED="1389355061857" MODIFIED="1389355065684">
<node TEXT="id unic pentru nod. Idee: sa avem schema pentru id, sau prefix, pentru a recunoaste nodurile cu persistare speciala. E.g. repo://er234ew; mm://32432efde" ID="ID_1756585984" CREATED="1389355066589" MODIFIED="1389355118142"/>
<node TEXT="salvar per user (poate chiar client side) care sunt nodurile deschise" ID="ID_1166191225" CREATED="1389356434803" MODIFIED="1389356444522"/>
<node TEXT="proprietati" ID="ID_1006571247" CREATED="1389356444983" MODIFIED="1389356447308">
<node TEXT="mod 1: primi props cand facem operatiuni" ID="ID_398517341" CREATED="1389356448371" MODIFIED="1389356464984"/>
<node TEXT="mod 2: la deschidere mindmap unde am multe noduri deschise: poate ar fi bine sa nu aduc props, pt ca e volum mare" ID="ID_1309465700" CREATED="1389356465182" MODIFIED="1389356488791"/>
</node>
</node>
<node TEXT="ce reprezinta Node, transferat pe client" ID="ID_1517376021" CREATED="1389355302729" MODIFIED="1389355313335">
<node TEXT="id" ID="ID_861518982" CREATED="1389355329180" MODIFIED="1389355332423"/>
<node TEXT="icon" ID="ID_996436572" CREATED="1389355314287" MODIFIED="1389355320199"/>
<node TEXT="title" ID="ID_730032165" CREATED="1389355320415" MODIFIED="1389355324279"/>
<node TEXT="map proprietati?" ID="ID_1818357874" CREATED="1389355373071" MODIFIED="1389355376818"/>
</node>
<node TEXT="api" ID="ID_1967055562" CREATED="1389355136972" MODIFIED="1389355137841">
<node TEXT="getRoot()" ID="ID_903760987" CREATED="1389355138747" MODIFIED="1389355143848"/>
<node TEXT="getChildren(id)" ID="ID_113132290" CREATED="1389355151760" MODIFIED="1389355175168"/>
<node TEXT="getNodeById()" ID="ID_874117331" CREATED="1389355181823" MODIFIED="1389355186899"/>
</node>
</node>
</node>
<node TEXT="Sincronizare&#xa;10/01/2014" STYLE_REF="Validated (Green)" ID="ID_1104013941" CREATED="1389354961560" MODIFIED="1389788100180">
<icon BUILTIN="group"/>
</node>
<node TEXT="Sincronizare&#xa;15/01/2014" STYLE_REF="Validated (Green)" ID="ID_1849707132" CREATED="1389788453005" MODIFIED="1389789019550">
<icon BUILTIN="group"/>
<node TEXT="Probleme in API Freeplane. Sunt dependente catre UI (AWT/Swing)." ID="ID_1627894378" CREATED="1389788864527" MODIFIED="1389788953747"/>
<node TEXT="Reflectie asupra mentinerii de map catre resurse deschise, low-level. (in implementarea mica/stateful" ID="ID_11873133" CREATED="1389788966547" MODIFIED="1389789018060"/>
</node>
<node TEXT="Sincronizare&#xa;16/01/2014" STYLE_REF="Validated (Green)" ID="ID_912488064" CREATED="1389868256273" MODIFIED="1389868266343">
<icon BUILTIN="group"/>
<node TEXT="task closed" ID="ID_1367735676" CREATED="1389868259948" MODIFIED="1389868264442"/>
</node>
</node>
<node TEXT="GH4 - Investigations related to Titan graph database, with Cassandra persistence" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1878951783" CREATED="1389789164584" MODIFIED="1390463609665" LINK="https://github.com/flower-platform/flower-platform-4/issues/4">
<icon BUILTIN="flag-green"/>
<node TEXT="Expected results" ID="ID_1421685343" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="Investigations" ID="ID_434275622" CREATED="1389384812898" MODIFIED="1389384950590">
<node TEXT="Can we use Titan graph database for the Flower Platform Server persistence layer?" ID="ID_310995778" CREATED="1389384838438" MODIFIED="1389384859289"/>
</node>
</node>
<node TEXT="Construction site" ID="ID_1072187475" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="Questions" STYLE_REF="New (Red)" ID="ID_942582898" CREATED="1389384864647" MODIFIED="1389384869397">
<icon BUILTIN="messagebox_warning"/>
<node TEXT="Accesul este rapid pentru un nod dupa id? Trebuie la afisare" ID="ID_370346609" CREATED="1389272667045" MODIFIED="1389272691997"/>
</node>
</node>
</node>
<node TEXT="GH6 - Prototype: MindMap Editor: Basic modification operations" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_587798628" CREATED="1389866982543" MODIFIED="1390463609660" LINK="https://github.com/flower-platform/flower-platform-4/issues/6">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_91003" CREATED="1389868468713" MODIFIED="1389868468720">
<node TEXT="RM8827 - GH6 - Prototype: MindMap Editor: Basic modification operations" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1091279628" CREATED="1389868468723" MODIFIED="1389868468731" LINK="http://csp1/redmine/issues/8827"/>
<node TEXT="RM8828 - [Review] GH6 - Prototype: MindMap Editor: Basic modification operations" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_965910641" CREATED="1389868468952" MODIFIED="1389868468962" LINK="http://csp1/redmine/issues/8828"/>
</node>
<node TEXT="Expected results" ID="ID_1861012193" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="Add child" ID="ID_196738108" CREATED="1389867849231" MODIFIED="1389867855373"/>
<node TEXT="Remove child" ID="ID_1767173193" CREATED="1389867855656" MODIFIED="1389867859941"/>
<node TEXT="Edit with inplace editor" ID="ID_847267323" CREATED="1389867860222" MODIFIED="1389867865173"/>
<node TEXT="DND" ID="ID_213360779" CREATED="1389867875935" MODIFIED="1389867892825"/>
<node TEXT="save mindmap" ID="ID_572202651" CREATED="1389867921462" MODIFIED="1389867924476"/>
</node>
<node TEXT="Construction site" ID="ID_26187551" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
</node>
</node>
<node TEXT="GH55 - HttpSessionListener" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_888822154" CREATED="1392289273592" MODIFIED="1393848668689" LINK="https://github.com/flower-platform/flower-platform-4/issues/55">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_1128047637" CREATED="1392305260036" MODIFIED="1392305260037">
<node TEXT="RM9006 - GH55 - HttpSessionListener" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_81641218" CREATED="1392305260038" MODIFIED="1392305260043" LINK="http://csp1/redmine/issues/9006"/>
<node TEXT="RM9007 - [Review] GH55 - HttpSessionListener" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1269809388" CREATED="1392305260542" MODIFIED="1392305260547" LINK="http://csp1/redmine/issues/9007"/>
</node>
<node TEXT="Expected results" ID="ID_1367783083" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="Can we get active sessions? In a standard manner? I.e. no hacks and cross server compatibility?" ID="ID_366507294" CREATED="1392289295338" MODIFIED="1392289320214"/>
<node TEXT="Listener" ID="ID_1006978083" CREATED="1392289326625" MODIFIED="1392289333383">
<node TEXT="what happens if" ID="ID_585448118" CREATED="1392289334312" MODIFIED="1392289337834">
<node TEXT="I have 3 sessions" ID="ID_1802405992" CREATED="1392289362470" MODIFIED="1392289367047"/>
<node TEXT="stop server &amp; start immediately; do I get &quot;created&quot; 3 times?" ID="ID_739156465" CREATED="1392289338619" MODIFIED="1392289371622"/>
<node TEXT="stop server &amp; start after timeout: do I get &quot;destroyed&quot; 3 times?" ID="ID_422622586" CREATED="1392289372050" MODIFIED="1392289385535"/>
</node>
</node>
</node>
<node TEXT="Tomcat persists the sessions across restarts by default, which means that all the sessions that were active when the server is stopped will be restored on restart. The clients will be able to reuse their sessions, so after restart there will be no &quot;created&quot; events, if there are any requests before the timeout (starting from the moment the server was restarted). After the timeout, the sessions will be destroyed; if there is any request from a client with an expired session, then a new session will be created." ID="ID_1628517803" CREATED="1392884647553" MODIFIED="1392884913287"/>
<node TEXT="If sessions persistence is disabled, then all active sessions will be destroyed on server stop." ID="ID_1314274010" CREATED="1392884916301" MODIFIED="1392884944996"/>
</node>
</node>
<node TEXT="GH1 - Create new repo and projects" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_239240879" CREATED="1389699138633" MODIFIED="1389699233516" LINK="https://github.com/flower-platform/flower-platform-4/issues/1">
<node TEXT="Redmine" ID="ID_1051834114" CREATED="1389781592842" MODIFIED="1389781592842">
<node TEXT="RM8812 - GH1 - Create new repo and projects" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1954845895" CREATED="1389781592842" MODIFIED="1389781592852" LINK="http://csp1/redmine/issues/8812"/>
</node>
<node TEXT="Construction site" ID="ID_1663303825" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="Proiecte" ID="ID_837280272" CREATED="1389867111917" MODIFIED="1389867114946">
<node TEXT=".core" ID="ID_121372598" CREATED="1389867117125" MODIFIED="1389867130587"/>
<node TEXT=".persistence.freeplane" ID="ID_1409931894" CREATED="1389867146949" MODIFIED="1389867163642"/>
<node TEXT=".flex_client.core" LOCALIZED_STYLE_REF="default" ID="ID_20422367" CREATED="1389867255612" MODIFIED="1389867680340"/>
<node TEXT=".flex_client.mindmap_editor" ID="ID_1650372279" CREATED="1389867169452" MODIFIED="1389867212673"/>
<node TEXT=".flex_client.diagram_editor" STYLE_REF="Planned (Orange)" ID="ID_1571194292" CREATED="1389867290916" MODIFIED="1389867307735"/>
<node TEXT=".flex_client.mobile" STYLE_REF="Planned (Orange)" ID="ID_1573596383" CREATED="1389867462163" MODIFIED="1389867472189"/>
</node>
<node TEXT="todo / now" ID="ID_472956702" CREATED="1390239380333" MODIFIED="1390239382985">
<node TEXT="comm client-server" STYLE_REF="Validated (Green)" ID="ID_391589447" CREATED="1390239385793" MODIFIED="1390401922137"/>
<node TEXT="embed messages.properties / flex" ID="ID_973565281" CREATED="1390239454843" MODIFIED="1390239509365"/>
<node TEXT="messages.properties for server; reuse mechanism using builder" ID="ID_880285767" CREATED="1390239509603" MODIFIED="1390239525595"/>
<node TEXT="comment/refactor" ID="ID_1644215261" CREATED="1390388197000" MODIFIED="1390388201499">
<node TEXT="ServiceLocator.faultHandler() uses Alert" ID="ID_986156993" CREATED="1390388203377" MODIFIED="1390388210894"/>
</node>
</node>
<node TEXT="todo / later" ID="ID_1286813810" CREATED="1390238305876" MODIFIED="1390239379435">
<node TEXT="Login" ID="ID_759615794" CREATED="1390239840084" MODIFIED="1390239842405"/>
<node TEXT="Mobile app" ID="ID_470532208" CREATED="1390239842923" MODIFIED="1390239846695"/>
<node TEXT="Eclipse" ID="ID_1174460552" CREATED="1390239880583" MODIFIED="1390239882715"/>
<node TEXT="" ID="ID_1902197781" CREATED="1390239883363" MODIFIED="1390239883363"/>
<node TEXT="Build procedure" ID="ID_1592714653" CREATED="1390238581788" MODIFIED="1390238586020">
<node TEXT="Don&apos;t use the public resources infrastructure." ID="ID_1569207478" CREATED="1390238586908" MODIFIED="1390238608540">
<node TEXT="Unzip the swc" ID="ID_1331559592" CREATED="1390238609448" MODIFIED="1390238614470"/>
<node TEXT="And register new mappings" ID="ID_1247093235" CREATED="1390238614668" MODIFIED="1390238647700"/>
<node TEXT="+ new flexHostAppConfig" ID="ID_1235641227" CREATED="1390238648088" MODIFIED="1390238661920"/>
</node>
</node>
</node>
<node TEXT="unde vom pune flexAppConfig?" ID="ID_877060621" CREATED="1390239078740" MODIFIED="1390239087942"/>
</node>
</node>
<node TEXT="GH2 - Prototype: Our data model with Titan, cluster/HA, performance tests" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1856184095" CREATED="1389624446783" MODIFIED="1389782335598" LINK="https://github.com/flower-platform/flower-platform-4/issues/2">
<node TEXT="Redmine" ID="ID_177805701" CREATED="1389782341908" MODIFIED="1389782341908">
<node TEXT="RM8815 - GH2 - Prototype: Our data model with Titan, cluster/HA, performance tests" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_538194824" CREATED="1389782341918" MODIFIED="1389782341918" LINK="http://csp1/redmine/issues/8815"/>
<node TEXT="RM8816 - [Review] GH2 - Prototype: Our data model with Titan, cluster/HA, performance tests" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_826119539" CREATED="1389782342148" MODIFIED="1389782342158" LINK="http://csp1/redmine/issues/8816"/>
</node>
<node TEXT="Expected results" ID="ID_977880570" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="Prototype, having our tree model as data model" ID="ID_200897210" CREATED="1389624582641" MODIFIED="1389624598507"/>
<node TEXT="Populated with a lot of data" ID="ID_154098447" CREATED="1389624598965" MODIFIED="1389624626069">
<node TEXT="Each node has 10 children" ID="ID_802096271" CREATED="1389624627067" MODIFIED="1389624634669"/>
<node TEXT="children 3, 6, 9 have 2 links: to the grandfather and grand-grandfather" ID="ID_944747625" CREATED="1389624645727" MODIFIED="1389624713978"/>
<node TEXT="About 10M or 100M" ID="ID_1237317455" CREATED="1389624958763" MODIFIED="1389624998396"/>
</node>
<node TEXT="&quot;Stresser&quot; program" ID="ID_908779072" CREATED="1389625028044" MODIFIED="1389625039696">
<node TEXT="console" ID="ID_624803675" CREATED="1389625041654" MODIFIED="1389625044958"/>
<node TEXT="cmd line arguments:" ID="ID_51744196" CREATED="1389625045266" MODIFIED="1389625188192">
<node TEXT="how many threads;" ID="ID_1020010445" CREATED="1389625188192" MODIFIED="1389625188192"/>
<node TEXT="periodicity" ID="ID_622001183" CREATED="1389625183302" MODIFIED="1389625183302"/>
<node TEXT="what level (i.e. how many nodes it fetches)" ID="ID_1287005664" CREATED="1389625195690" MODIFIED="1389625216984"/>
</node>
<node TEXT="each thread" ID="ID_1839866104" CREATED="1389625128740" MODIFIED="1389625173112">
<node TEXT="gets all subtree starting with the specified level" ID="ID_1526225114" CREATED="1389625242818" MODIFIED="1389625245090"/>
<node TEXT="waits" ID="ID_378548096" CREATED="1389625245508" MODIFIED="1389625247610"/>
<node TEXT="repeat for the &quot;neighbour&quot; node" ID="ID_533509901" CREATED="1389625248088" MODIFIED="1389625280300"/>
<node TEXT="log the request time" ID="ID_826850362" CREATED="1389625283168" MODIFIED="1389625289522"/>
</node>
<node TEXT="synchronized print method" ID="ID_995058238" CREATED="1389625682737" MODIFIED="1389625702869"/>
<node TEXT="each line: 34s, thread 1, 25ms" ID="ID_1072254923" CREATED="1389625720647" MODIFIED="1389625757823"/>
</node>
<node TEXT="HA use cases" ID="ID_214635422" CREATED="1389625886055" MODIFIED="1389625896037">
<node TEXT="add new node in cluster (our prog + titan + cassandra), we expect to 1/2 request time" ID="ID_1657290660" CREATED="1389625897267" MODIFIED="1389626000890">
<node TEXT="optional: difference titan+cassandra embeded" ID="ID_1198313804" CREATED="1389626217306" MODIFIED="1389626230228"/>
</node>
<node TEXT="perform update on a 2 node cluster, 1 shutdown during update, power on again, shudtown the other =&gt; data should be consistent" ID="ID_909046775" CREATED="1389625933841" MODIFIED="1389626037326"/>
</node>
</node>
<node TEXT="Construction site" ID="ID_1203185397" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
</node>
<node TEXT="Sincronizare&#xa;15/01/2014" STYLE_REF="Validated (Green)" ID="ID_1177880932" CREATED="1389783104024" MODIFIED="1389783878561">
<icon BUILTIN="group"/>
<node TEXT="Context: 100 thread care fac cereri" ID="ID_1092083991" CREATED="1389783832490" MODIFIED="1389783838741"/>
<node TEXT="In mod dubios, dupa aprox 2 minute de lucru, timpul mediu urca de la aprox 500 ms, la 1s, 5s, 10s, etc" ID="ID_1259298256" CREATED="1389783804320" MODIFIED="1389783859251"/>
<node TEXT="De investigat daca nu se datoreaza saturarii heapului." ID="ID_98194085" CREATED="1389783859459" MODIFIED="1389783875101"/>
</node>
<node TEXT="Sincronizare&#xa;17/01/2014" STYLE_REF="Validated (Green)" ID="ID_1813562546" CREATED="1389975958514" MODIFIED="1389975961754">
<icon BUILTIN="group"/>
<node TEXT="A fost incercata o abordare strict Casandra, cu un model de date gen SQL (e.g. o singura tabela pt noduri)" ID="ID_453628463" CREATED="1389975964343" MODIFIED="1389976001614"/>
<node TEXT="Nici o optimizare" ID="ID_1649997437" CREATED="1389976001883" MODIFIED="1389976006064"/>
<node TEXT="Timp mare de incarcare (&gt;15 min pt ~ 11k noduri)" ID="ID_59767147" CREATED="1389976006303" MODIFIED="1389976025044"/>
<node TEXT="Timpi mari la cererea unui graf (2s pt ~100 noduri)" ID="ID_1452495569" CREATED="1389976025373" MODIFIED="1389976042004"/>
<node TEXT="Probleme la clustering" ID="ID_1561079946" CREATED="1389976042322" MODIFIED="1389976048524">
<node TEXT="Deconectare nod: se strica totul" ID="ID_864660061" CREATED="1389976049453" MODIFIED="1389976056084"/>
<node TEXT="Conectare nod suplimentar: scade load / CPU, dar nu creste viteza. Din contra, scade." ID="ID_114914374" CREATED="1389976056292" MODIFIED="1389976083865"/>
</node>
</node>
<node TEXT="Sincronizare&#xa;27/01/2014" STYLE_REF="Validated (Green)" ID="ID_1869046617" CREATED="1390829485061" MODIFIED="1390829487755">
<icon BUILTIN="group"/>
<node TEXT="Terminat si investigatii cu HBase" ID="ID_196986085" CREATED="1390829489509" MODIFIED="1390829497063"/>
<node TEXT="Rezultate multumitoare (mai mari ca Postgres, dar nu cu mult)" ID="ID_518063208" CREATED="1390829497267" MODIFIED="1390829507781"/>
</node>
</node>
<node TEXT="GH5 - Investigations related to an integration with Idea" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1036532669" CREATED="1389789384745" MODIFIED="1389789404487" LINK="https://github.com/flower-platform/flower-platform-4/issues/5">
<node TEXT="Redmine" ID="ID_534435727" CREATED="1389789410987" MODIFIED="1389789410987">
<node TEXT="RM8822 - GH5 - Investigations related to an integration with Idea" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1850677770" CREATED="1389789410987" MODIFIED="1389789410997" LINK="http://csp1/redmine/issues/8822"/>
<node TEXT="RM8823 - [Review] GH5 - Investigations related to an integration with Idea" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1062530681" CREATED="1389789411227" MODIFIED="1389789411237" LINK="http://csp1/redmine/issues/8823"/>
</node>
<node TEXT="Expected results" ID="ID_326741948" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
</node>
<node TEXT="Construction site" ID="ID_78200973" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="plugin that opens a file (e.g. .notations) that opens a browser. It communicates with the browser via JS." ID="ID_1459352599" CREATED="1384183154425" MODIFIED="1384183184625"/>
<node TEXT="editor.dirty" ID="ID_54088633" CREATED="1384183190092" MODIFIED="1384183193601">
<node TEXT="show *" ID="ID_1605681668" CREATED="1384247591081" MODIFIED="1384247594916"/>
<node TEXT="intercept save action" ID="ID_633118933" CREATED="1384247595160" MODIFIED="1384247605063"/>
</node>
<node TEXT="start the osgi framework with our plugins from IDEA" ID="ID_1112164761" CREATED="1384183194260" MODIFIED="1384183210788">
<node TEXT="http://stackoverflow.com/questions/904050/how-to-run-an-osgi-framework-within-usual-java-code" ID="ID_1788478345" CREATED="1384183228927" MODIFIED="1384183228927" LINK="http://stackoverflow.com/questions/904050/how-to-run-an-osgi-framework-within-usual-java-code"/>
<node TEXT="http://njbartlett.name/2011/03/07/embedding-osgi.html" ID="ID_1584500669" CREATED="1384183234917" MODIFIED="1384183234917" LINK="http://njbartlett.name/2011/03/07/embedding-osgi.html"/>
</node>
<node TEXT="open a diagram" ID="ID_601044272" CREATED="1384183211084" MODIFIED="1384183216751"/>
<node TEXT="Dirty and save" ID="ID_1216390791" CREATED="1389022801958" MODIFIED="1389022844711"/>
<node TEXT="Open diagram logic" ID="ID_49575463" CREATED="1389022847641" MODIFIED="1389022861866"/>
</node>
<node TEXT="Sincronizare&#xa;03/12/2013" STYLE_REF="Validated (Green)" ID="ID_1848153977" CREATED="1386065930395" MODIFIED="1389789511627">
<icon BUILTIN="group"/>
<node TEXT="New plugin: .idea" ID="ID_1113522099" CREATED="1386065945598" MODIFIED="1386065982531"/>
<node TEXT="copy FrameworkLauncher* from .web" ID="ID_28028773" CREATED="1386065983302" MODIFIED="1386065996363"/>
<node TEXT="launch eclipse from within idea, based on an .ini file generated by a launch config (similar to the one in .web)" ID="ID_1038320855" CREATED="1386065996598" MODIFIED="1386066032235"/>
<node TEXT="mappings for servlets, like .eclipse" ID="ID_632489527" CREATED="1386066034917" MODIFIED="1386066053842"/>
<node TEXT="in plugin.start: start jetty (like in .eclipse)" ID="ID_239583299" CREATED="1386066054085" MODIFIED="1386066083498"/>
<node TEXT="classes needed to work with VirtualFile" ID="ID_1117210010" CREATED="1387377897076" MODIFIED="1387378380914"/>
<node TEXT="action that creates a new diagram" ID="ID_578895568" CREATED="1387378404572" MODIFIED="1387378447729"/>
<node TEXT="modificari" STYLE_REF="Planned (Orange)" ID="ID_208958633" CREATED="1387377731901" MODIFIED="1387377895153">
<node TEXT="folosim cale absoluta in idea pentru resurse" LOCALIZED_STYLE_REF="defaultstyle.note" ID="ID_1874743973" CREATED="1387377904733" MODIFIED="1387378276644"/>
<node TEXT="in EditorOperationsService.DecodedLink am inlocuit  &quot;:&quot; cu &quot;|&quot; ca sa putem folosi calea absoluta" LOCALIZED_STYLE_REF="defaultstyle.note" ID="ID_1915077073" CREATED="1387377940548" MODIFIED="1387378341106"/>
</node>
<node TEXT="action that adds elements on diagram" ID="ID_1562951441" CREATED="1387378490700" MODIFIED="1387378565914"/>
</node>
</node>
<node TEXT="GH8 - Prototype/Tomcat HA 1: clustering and load balancing" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_455253975" CREATED="1389389401084" MODIFIED="1389949478442" LINK="https://github.com/flower-platform/flower-platform-4/issues/8">
<node TEXT="Redmine" ID="ID_904106142" CREATED="1389949497382" MODIFIED="1389949497382">
<node TEXT="RM8834 - GH8 - Prototype/Tomcat HA 1: clustering and load balancing" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1754302103" CREATED="1389949497392" MODIFIED="1389949497392" LINK="http://csp1/redmine/issues/8834"/>
<node TEXT="RM8835 - [Review] GH8 - Prototype/Tomcat HA 1: clustering and load balancing" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1740790773" CREATED="1389949497792" MODIFIED="1389949497792" LINK="http://csp1/redmine/issues/8835"/>
</node>
<node TEXT="Construction site" ID="ID_1720057972" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
</node>
<node TEXT="Expected results" ID="ID_1394223759" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="Configuration with multiple servers" ID="ID_1993911586" CREATED="1389950437032" MODIFIED="1389950447058"/>
<node TEXT="Illustrate that calls are handled by various servers" ID="ID_799016190" CREATED="1389950447353" MODIFIED="1389950473582"/>
<node TEXT="Monitoring?" ID="ID_1933052938" CREATED="1389950487837" MODIFIED="1389950500299"/>
<node TEXT="Use a simple servlet" ID="ID_1141971863" CREATED="1389950542461" MODIFIED="1389950545895"/>
</node>
<node TEXT="Info" ID="ID_176566598" CREATED="1390230181401" MODIFIED="1390230185423">
<node TEXT="http://www.ramkitech.com/2012/10/tomcat-clustering-series-simple-load.html" ID="ID_891171067" CREATED="1390230186385" MODIFIED="1390230237317" LINK="http://www.ramkitech.com/2012/10/tomcat-clustering-series-simple-load.html"/>
<node TEXT="http://www.tutorialspoint.com/white-papers/load-balancing-and-scalability-via-tomcat-clusters.htm" ID="ID_778488740" CREATED="1390230219497" MODIFIED="1390230237314" LINK="http://www.tutorialspoint.com/white-papers/load-balancing-and-scalability-via-tomcat-clusters.htm"/>
<node TEXT="http://www.mulesoft.com/tomcat-clustering" ID="ID_1753967163" CREATED="1390230225786" MODIFIED="1390230237309" LINK="http://www.mulesoft.com/tomcat-clustering"/>
</node>
<node TEXT="Wiki" ID="ID_757142769" CREATED="1390486179835" MODIFIED="1390486182966">
<node TEXT="http://csp1/dokuwiki/proiecte/flower-platform4/server/tomcat_clustering_configuration" ID="ID_349869407" CREATED="1390486169716" MODIFIED="1390486169716" LINK="http://csp1/dokuwiki/proiecte/flower-platform4/server/tomcat_clustering_configuration"/>
</node>
</node>
<node TEXT="GH9 - Prototype/Tomcat HA 2: session replication using DB/Cassandra" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1716062309" CREATED="1389389464514" MODIFIED="1389950141423" LINK="https://github.com/flower-platform/flower-platform-4/issues/9">
<node TEXT="Redmine" ID="ID_1346224868" CREATED="1390384021718" MODIFIED="1390384021744">
<node TEXT="RM8846 - GH9 - Prototype/Tomcat HA 2: session replication using DB/Cassandra" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_329645413" CREATED="1390384021746" MODIFIED="1390384021772" LINK="http://csp1/redmine/issues/8846"/>
<node TEXT="RM8847 - [Review] GH9 - Prototype/Tomcat HA 2: session replication using DB/Cassandra" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_53747263" CREATED="1390384022830" MODIFIED="1390384022841" LINK="http://csp1/redmine/issues/8847"/>
</node>
<node TEXT="Expected results" ID="ID_970185065" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="Server uses session" ID="ID_1863853333" CREATED="1389950559441" MODIFIED="1389950565423"/>
<node TEXT="Replicate session between servers" ID="ID_479039474" CREATED="1389950568755" MODIFIED="1389950575524"/>
</node>
<node TEXT="http://code.google.com/a/apache-extras.org/p/tomcat-cassandra/" ID="ID_1901331919" CREATED="1390230146044" MODIFIED="1390230158305" LINK="http://code.google.com/a/apache-extras.org/p/tomcat-cassandra/"/>
</node>
<node TEXT="GH83 - GitHub Integration - Orion Diff UI" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_826628725" CREATED="1393429085622" MODIFIED="1393429277450" LINK="https://github.com/flower-platform/flower-platform-4/issues/83">
<node TEXT="Redmine" ID="ID_1019393230" CREATED="1393429289892" MODIFIED="1393429289892">
<node TEXT="RM9059 - GH83 - GitHub Integration - Orion Diff UI" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1571932628" CREATED="1393429289902" MODIFIED="1393429289902" LINK="http://csp1/redmine/issues/9059"/>
<node TEXT="RM9060 - [Review] GH83 - GitHub Integration - Orion Diff UI" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_794675183" CREATED="1393429290232" MODIFIED="1393429290232" LINK="http://csp1/redmine/issues/9060"/>
</node>
</node>
<node TEXT="GH84 - GitHub Integration - CodeSync and API" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_982618184" CREATED="1393429112412" MODIFIED="1393429281522" LINK="https://github.com/flower-platform/flower-platform-4/issues/84">
<node TEXT="Redmine" ID="ID_1469765852" CREATED="1393429299022" MODIFIED="1393429299022">
<node TEXT="RM9061 - GH84 - GitHub Integration - CodeSync and API" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1217464950" CREATED="1393429299022" MODIFIED="1393429299022" LINK="http://csp1/redmine/issues/9061"/>
<node TEXT="RM9062 - [Review] GH84 - GitHub Integration - CodeSync and API" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_399147570" CREATED="1393429299352" MODIFIED="1393429299362" LINK="http://csp1/redmine/issues/9062"/>
</node>
<node TEXT="TODO" ID="ID_1485546011" CREATED="1394036560786" MODIFIED="1394036563827">
<node TEXT="decoratori egit dreapta-jos" ID="ID_148889325" CREATED="1394036563827" MODIFIED="1394036571957"/>
<node TEXT="creare mai multe elemente noi din model -&gt; setat un id artificial care sa fie apoi suprascris dupa ce se creaza element pe right" ID="ID_796781477" CREATED="1394036579966" MODIFIED="1394036594869"/>
</node>
</node>
<node TEXT="GH221 - Migrate first MDA rule" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_830229508" CREATED="1396973557600" MODIFIED="1396973657027" LINK="https://github.com/flower-platform/flower-platform-4/issues/221">
<node TEXT="Redmine" ID="ID_162746381" CREATED="1396973662308" MODIFIED="1396973662324">
<node TEXT="RM9297 - GH221 - Migrate first MDA rule" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_969602592" CREATED="1396973662324" MODIFIED="1396973662324" LINK="http://csp1/redmine/issues/9297"/>
</node>
<node TEXT="Construction site" ID="ID_1638957923" CREATED="1389423489709" MODIFIED="1393273610509">
<icon BUILTIN="mindmap"/>
<node TEXT="MDA" ID="ID_828739193" CREATED="1395821871675" MODIFIED="1395821872882">
<node TEXT="Markeri validare?" ID="ID_1456273019" CREATED="1395823659597" MODIFIED="1395823663259">
<node TEXT="Regulile lucrau in 2 etape. 1) in care se puneau niste markeri (de tip validare), cu posibilitate de quick fix. Si 2) rularea de quick-fix" ID="ID_385413821" CREATED="1395822520738" MODIFIED="1395822943080"/>
<node TEXT="Actiunile efectuate ar trebui sa trimita pe client tree-ul cu rezultatele." ID="ID_1914711735" CREATED="1395826659069" MODIFIED="1395826682501"/>
</node>
<node TEXT="Sistem temporar de link-uri" ID="ID_1800331584" CREATED="1395910687175" MODIFIED="1395910993191"/>
</node>
<node TEXT="Afisare in root, resursa code, si resursa MDA" ID="ID_1279925273" CREATED="1396973731861" MODIFIED="1396973916231" VSHIFT="40"/>
<node TEXT="Trasat link manual intre pachetul java .entities, si pachetul mda &quot;Data Model&quot;" ID="ID_603914659" CREATED="1396973805111" MODIFIED="1396973848987">
<node TEXT="cum facem asta?" ID="ID_1007601097" CREATED="1396974069819" MODIFIED="1396974074220"/>
</node>
<node TEXT="Registru de reguli?" ID="ID_1114614774" CREATED="1395823656374" MODIFIED="1395823659067">
<node TEXT="Facem un registru de reguli? Impreuna cu niste controlleri speciali care se uita aici? Sau modificam controllerul sa fie o interfata, si integram mai multe intr-o regula abstracta? Astfel am inregistra direct controlleri" ID="ID_1126011160" CREATED="1395821878002" MODIFIED="1395822336228"/>
<node TEXT="Trebuie sa ne gandim si cum putem sa spargem in 2 functionalitatea, pentru a avea uni-dir pentru Community, si bi-dir pentru Pro." ID="ID_939469287" CREATED="1396974372017" MODIFIED="1396974407603"/>
<node TEXT="De vazut cum procesam atunci &quot;added, removed&quot;" ID="ID_1841432194" CREATED="1396974447615" MODIFIED="1396974460752"/>
</node>
<node TEXT="Regula creaza sau actualizeaza markeri" ID="ID_1593454431" CREATED="1396974476132" MODIFIED="1396974511647">
<node TEXT="Parca aveam si cod ca in cazul in care apare elementul dorit, sa dispara automat markerul" ID="ID_998663904" CREATED="1396974515537" MODIFIED="1396974533417"/>
</node>
<node TEXT="Afisare de markeri" ID="ID_150243214" CREATED="1396974542120" MODIFIED="1396974551466">
<node TEXT="Decorare elemente existente" ID="ID_1742623398" CREATED="1396974592446" MODIFIED="1396974596722"/>
<node TEXT="Fereastra de aparitie markeri" ID="ID_386871062" CREATED="1396974596993" MODIFIED="1396974630675"/>
<node TEXT="Idee: sa avem o zona in care se afiseaza update-urile. Ca entitati si cine face operatiunea. Tot aici ar aparea si modificarile referitoare la markeri de validare (daca alt cineva face asta). Daca noi suntem initiatorii, poate ne-ar interesa sa se faca si un popup." ID="ID_1586582883" CREATED="1396974687434" MODIFIED="1396974771895"/>
</node>
<node TEXT="Actiune care &quot;rezolva&quot; un marker" ID="ID_1868382728" CREATED="1396975062061" MODIFIED="1396975073405">
<node TEXT="Ar trebui sa facem si cazul: creare ent Java =&gt; creare ent MDA =&gt; creare de serviciu;&#xa;Adica un marker care rezolvat, creaza alti markeri" ID="ID_1104128338" CREATED="1396975095126" MODIFIED="1396975215219"/>
</node>
<node TEXT="Sistem automatic de rezolvare de markeri" ID="ID_912310526" CREATED="1396975234943" MODIFIED="1396975349999">
<node TEXT="Trebuie foarte clar ca dupa procesul de rezolvare, sa vad exact ceea ce s-a intamplat. Poate tot bazat pe" ID="ID_1735502027" CREATED="1396975257290" MODIFIED="1396976355598"/>
<node TEXT="O regula clara: de impartit in &quot;sesiuni&quot;. Intr-o sesiune, o regula nu are voie sa creeze. Doar sa genereze markeri." ID="ID_586575949" CREATED="1396975379404" MODIFIED="1396975439435"/>
<node TEXT="Procesatorul ar trebui pus undeva in cadrul sistemului de invocation..." ID="ID_1094846925" CREATED="1396975440408" MODIFIED="1396975455885"/>
<node TEXT="Trebuie sa intuim mecanism prin care sa evitam procesarea infinita" ID="ID_935611646" CREATED="1396975497394" MODIFIED="1396975514666"/>
</node>
</node>
</node>
<node TEXT="GH234 - Shared cookie for Flex app and embedded HTML" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_246775360" CREATED="1397229278761" MODIFIED="1397465939207" LINK="https://github.com/flower-platform/flower-platform-4/issues/234">
<node TEXT="Web: session ID is shared" ID="ID_832497341" CREATED="1397229587566" MODIFIED="1397229595864"/>
<node TEXT="Mobile: session ID is NOT shared" ID="ID_191376232" CREATED="1397229652174" MODIFIED="1397464544284"/>
<node TEXT="Redmine" FOLDED="true" ID="ID_1291117249" CREATED="1397465948117" MODIFIED="1397465948117">
<node TEXT="RM9314 - GH234 - Shared cookie for Flex app and embedded HTML" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_137029896" CREATED="1397465948117" MODIFIED="1397465948117" LINK="http://csp1/redmine/issues/9314"/>
<node TEXT="RM9315 - [Review] GH234 - Shared cookie for Flex app and embedded HTML" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1171072689" CREATED="1397465948647" MODIFIED="1397465948657" LINK="http://csp1/redmine/issues/9315"/>
</node>
</node>
</node>
<node TEXT="codesync" STYLE_REF="In progress (Yellow)" FOLDED="true" POSITION="right" ID="ID_1992057714" CREATED="1390829561756" MODIFIED="1391792765113">
<node TEXT="GH19 - CodeSync first iteration: migrate existing code &amp; algorithm" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_838029624" CREATED="1390829596805" MODIFIED="1392304351760" LINK="https://github.com/flower-platform/flower-platform-4/issues/19">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_62759308" CREATED="1390832882412" MODIFIED="1390832882422">
<node TEXT="RM8902 - GH19 - CodeSync first iteration: migrate existing code &amp; algorithm" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_69250566" CREATED="1390832882422" MODIFIED="1390832882452" LINK="http://csp1/redmine/issues/8902"/>
<node TEXT="RM8903 - [Review] GH19 - CodeSync first iteration: migrate existing code &amp; algorithm" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_89868159" CREATED="1390832882772" MODIFIED="1390832882782" LINK="http://csp1/redmine/issues/8903"/>
</node>
<node TEXT="#Expected results" ID="ID_371538064" CREATED="1389360413465" MODIFIED="1390558909010">
<icon BUILTIN="revision"/>
<node TEXT="Copy cs projects" ID="ID_482486884" CREATED="1390829649950" MODIFIED="1390829659351"/>
<node TEXT="Including tests; but no need to function yet" ID="ID_329599266" CREATED="1390829659549" MODIFIED="1390829671145"/>
<node TEXT="Change package names (com... -&gt; org...)" ID="ID_548481077" CREATED="1390829671358" MODIFIED="1390829682584"/>
<node TEXT="Improvement remarks?" ID="ID_1180012590" CREATED="1390830051247" MODIFIED="1390830054916"/>
<node TEXT="Sync/read" ID="ID_1640224670" CREATED="1390830125545" MODIFIED="1390830137631"/>
</node>
</node>
<node TEXT="GH20 - CodeSync second iteration: modify props and synchronize" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_185994372" CREATED="1390830201406" MODIFIED="1392304351760" LINK="https://github.com/flower-platform/flower-platform-4/issues/20">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_845319157" CREATED="1391070894080" MODIFIED="1391070894086">
<node TEXT="RM8916 - GH20 - CodeSync second iteration: modify props and synchronize" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_286853340" CREATED="1391070894088" MODIFIED="1391070894107" LINK="http://csp1/redmine/issues/8916"/>
<node TEXT="RM8917 - [Review] GH20 - CodeSync second iteration: modify props and synchronize" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_746099256" CREATED="1391070896652" MODIFIED="1391070896661" LINK="http://csp1/redmine/issues/8917"/>
</node>
<node TEXT="#Expected results" FOLDED="true" ID="ID_633768254" CREATED="1389360413465" MODIFIED="1390558909010">
<icon BUILTIN="revision"/>
<node TEXT="Modify properties =&gt; interceptori care actualizeaza proprietati de tip .oldValue" ID="ID_497354072" CREATED="1390830238570" MODIFIED="1390830263717"/>
<node TEXT="added, removed" ID="ID_318635519" CREATED="1390830361003" MODIFIED="1390830363068"/>
<node TEXT="Sync/write" ID="ID_845522118" CREATED="1390830395285" MODIFIED="1390830400826"/>
</node>
<node TEXT="Construction site" FOLDED="true" ID="ID_711414989" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="New NTD" STYLE_REF="Planned (Orange)" ID="ID_805036497" CREATED="1391531629329" MODIFIED="1391610324895">
<node TEXT="&lt;T&gt; T getController(&quot;feature&quot;)" ID="ID_1161408495" CREATED="1391531633749" MODIFIED="1391606727318"/>
<node TEXT="&lt;T&gt; List&lt;T&gt; getControllers(&quot;chiPro&quot;)" ID="ID_1773563509" CREATED="1391531817058" MODIFIED="1391606750648"/>
<node TEXT="Map&lt;String, (Object, boolean)&gt;: tip, contrl/lista de, cache procesat" ID="ID_931942905" CREATED="1391531892019" MODIFIED="1391531984450"/>
</node>
<node TEXT="priority/order" STYLE_REF="Planned (Orange)" ID="ID_124236530" CREATED="1391602627020" MODIFIED="1391610334516">
<node TEXT="de redenumit in orderIndex" ID="ID_1744849578" CREATED="1391602631700" MODIFIED="1391602651292"/>
<node TEXT="-100 000" ID="ID_771071687" CREATED="1391602661450" MODIFIED="1391602676962"/>
<node TEXT="50 000" ID="ID_1451762322" CREATED="1391602738602" MODIFIED="1391602740334"/>
<node TEXT="100 000" ID="ID_298649949" CREATED="1391602681960" MODIFIED="1391602687252"/>
</node>
<node TEXT="org.flowerplatform.core.node.remote.NodeService.getPropertiesToDisplay(String)" STYLE_REF="Planned (Orange)" ID="ID_1933554915" CREATED="1391603616908" MODIFIED="1391610361676">
<node TEXT="getPropertyDescriptors()" ID="ID_1597078268" CREATED="1391603622406" MODIFIED="1391603632988"/>
<node TEXT="de reutilizat sistemul de ordonare" ID="ID_497156515" CREATED="1391603633446" MODIFIED="1391603648098"/>
<node TEXT="NodeController -&gt; OrderedElement" ID="ID_1844015095" CREATED="1391603708996" MODIFIED="1391603719418"/>
<node TEXT="putem folosi getControllersForTypeAndCategories" ID="ID_1878832494" CREATED="1391603745246" MODIFIED="1391603748998"/>
<node TEXT="redenumim?" ID="ID_1464996505" CREATED="1391603787417" MODIFIED="1391603789859"/>
</node>
<node TEXT="commit" ID="ID_845242283" CREATED="1391610345475" MODIFIED="1391610348017"/>
<node TEXT="org.flowerplatform.codesync.code.CodeSyncCodePlugin.getNode(File, File, String, boolean)" STYLE_REF="Planned (Orange)" ID="ID_736589546" CREATED="1391443741540" MODIFIED="1391610764131">
<node TEXT="mai ne trebuie logica de get or create....?" ID="ID_1337167155" CREATED="1391443747108" MODIFIED="1391443760320"/>
</node>
<node TEXT="CSAlgRunner" STYLE_REF="Planned (Orange)" ID="ID_629602395" CREATED="1391443534738" MODIFIED="1391610764131">
<node TEXT="nu ne permite sa avem mai multe tehno in ac src dir; e.g. js si html" ID="ID_1367786291" CREATED="1391443543138" MODIFIED="1391443560400"/>
</node>
<node TEXT="TypeProvider" STYLE_REF="Planned (Orange)" ID="ID_193303457" CREATED="1391606931048" MODIFIED="1391610764141">
<node TEXT="getType(Object):String" ID="ID_882162493" CREATED="1391606943089" MODIFIED="1391606997971"/>
<node TEXT="composed type provider" ID="ID_910646592" CREATED="1391610667357" MODIFIED="1391610672411"/>
<node TEXT="tinuti in map&lt;technologie&gt;" ID="ID_1568967001" CREATED="1391610672649" MODIFIED="1391610680601"/>
<node TEXT="la sync: se combina tehno disponibile" ID="ID_1782626432" CREATED="1391610681009" MODIFIED="1391610693371"/>
</node>
<node TEXT="commit" ID="ID_1767194166" CREATED="1391692059977" MODIFIED="1391692061629"/>
<node TEXT="mod de rulare &quot;dintr-o bucata&quot;; simultan reverse si forward" STYLE_REF="Planned (Orange)" ID="ID_877735004" CREATED="1391446347497" MODIFIED="1391610780030"/>
</node>
<node TEXT="Sincronizare&#xa;05/02/2014" STYLE_REF="Validated (Green)" ID="ID_75235885" CREATED="1391607419121" MODIFIED="1391607422261">
<icon BUILTIN="group"/>
</node>
</node>
<node TEXT="GH56 - 1 step sync issue, because of the delayed save. Try save during sync" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_929852157" CREATED="1392303758700" MODIFIED="1393848468699" LINK="https://github.com/flower-platform/flower-platform-4/issues/56">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_1355722804" CREATED="1393414422282" MODIFIED="1393414427835">
<node TEXT="RM9057 - GH56 - 1 step sync issue, because of the delayed save. Try save during sync" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1364615695" CREATED="1393414422290" MODIFIED="1393414422315" LINK="http://csp1/redmine/issues/9057"/>
<node TEXT="RM9058 - [Review] GH56 - 1 step sync issue, because of the delayed save. Try save during sync" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_883009873" CREATED="1393414422819" MODIFIED="1393414422822" LINK="http://csp1/redmine/issues/9058"/>
</node>
</node>
<node TEXT="GH58 - Reintroduce the codeSync unit tests" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_299659123" CREATED="1392304057664" MODIFIED="1395230530746" LINK="https://github.com/flower-platform/flower-platform-4/issues/58">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_1552331374" CREATED="1392722271006" MODIFIED="1392722271012">
<node TEXT="RM9022 - GH58 - Reintroduce the codeSync unit tests" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1279945631" CREATED="1392722271014" MODIFIED="1392722271043" LINK="http://csp1/redmine/issues/9022"/>
<node TEXT="RM9023 - [Review] GH58 - Reintroduce the codeSync unit tests" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_689973011" CREATED="1392722271609" MODIFIED="1392722271612" LINK="http://csp1/redmine/issues/9023"/>
</node>
</node>
<node TEXT="GH80 - Refactor constants; only set &quot;type&quot; at node construction" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1004072800" CREATED="1393258641328" MODIFIED="1393848495532" LINK="https://github.com/flower-platform/flower-platform-4/issues/80">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_884987143" CREATED="1393258739013" MODIFIED="1393258739017">
<node TEXT="RM9050 - GH80 - Refactor constants; only set &quot;type&quot; at node construction" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_704160221" CREATED="1393258739019" MODIFIED="1393258739029" LINK="http://csp1/redmine/issues/9050"/>
<node TEXT="RM9051 - [Review] GH80 - Refactor constants; only set &quot;type&quot; at node construction" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_8087929" CREATED="1393258739554" MODIFIED="1393258739561" LINK="http://csp1/redmine/issues/9051"/>
</node>
</node>
<node TEXT="CodeSync Controllers" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_72880985" CREATED="1391791932648" MODIFIED="1393666827272">
<node TEXT="On property modification =&gt; save previous value (e.g. visibility.original)" STYLE_REF="Validated (Green)" ID="ID_991060155" CREATED="1391791950438" MODIFIED="1391792774038"/>
<node TEXT="On add =&gt; added flag" STYLE_REF="Validated (Green)" ID="ID_82410866" CREATED="1391792043200" MODIFIED="1391792774040"/>
<node TEXT="On modification (add, set, mark remove), propagates codesync flags" STYLE_REF="Validated (Green)" ID="ID_1536200420" CREATED="1391792059380" MODIFIED="1393666827281">
<node TEXT="sync" ID="ID_1662246359" CREATED="1391792387203" MODIFIED="1391792391297"/>
<node TEXT="childrenSync" ID="ID_417016327" CREATED="1391792391545" MODIFIED="1391792394307"/>
<node TEXT="conflict" ID="ID_1385134416" CREATED="1391792394655" MODIFIED="1391792435088"/>
<node TEXT="childrenConflict" ID="ID_326727975" CREATED="1391792435526" MODIFIED="1391792442578"/>
<node TEXT="GH41 - CodeSync flags and propagation" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_934401889" CREATED="1391792671522" MODIFIED="1393666808400" LINK="https://github.com/flower-platform/flower-platform-4/issues/41">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_712160590" CREATED="1392020157412" MODIFIED="1392020157422">
<node TEXT="RM8966 - GH41 - CodeSync flags and propagation" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1947322799" CREATED="1392020157422" MODIFIED="1392020157452" LINK="http://csp1/redmine/issues/8966"/>
<node TEXT="RM8967 - [Review] GH41 - CodeSync flags and propagation" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_837702009" CREATED="1392020157832" MODIFIED="1392020157842" LINK="http://csp1/redmine/issues/8967"/>
</node>
<node TEXT="Problems" ID="ID_1482186374" CREATED="1392110662041" MODIFIED="1392110670817">
<node TEXT="How to enable the logic only for codesync props? E.g. not for icon, etc" ID="ID_362322" CREATED="1392110671899" MODIFIED="1392110695537"/>
</node>
</node>
</node>
</node>
<node TEXT="GH104 - CodeSync flags propagation" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_973157519" CREATED="1393938980756" MODIFIED="1393939083590" LINK="https://github.com/flower-platform/flower-platform-4/issues/104"/>
</node>
<node TEXT="codesync.java" STYLE_REF="Validated (Green)" FOLDED="true" POSITION="right" ID="ID_473499051" CREATED="1392110065213" MODIFIED="1393666903086">
<node TEXT="Controllers and Descriptors" STYLE_REF="Validated (Green)" ID="ID_1579449342" CREATED="1392110183252" MODIFIED="1393666901130">
<node TEXT="Icons" STYLE_REF="Validated (Green)" ID="ID_39022144" CREATED="1392110189098" MODIFIED="1393666912549">
<node TEXT="GH44 - Controllers for icons" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1625091972" CREATED="1392110206646" MODIFIED="1393666886266" LINK="https://github.com/flower-platform/flower-platform-4/issues/44">
<icon BUILTIN="flag-green"/>
<node TEXT="Expected results" ID="ID_1774940981" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
</node>
<node TEXT="Construction site" ID="ID_979402688" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT=".core" ID="ID_1857457223" CREATED="1392110528852" MODIFIED="1392110531204">
<node TEXT="ConstantValuePropertySetter(property, value)" STYLE_REF="Code" ID="ID_625858529" CREATED="1392110257591" MODIFIED="1392110512929"/>
</node>
</node>
<node TEXT="Redmine" ID="ID_1719282867" CREATED="1392118494899" MODIFIED="1392118494905">
<node TEXT="RM8976 - GH44 - Controllers for icons" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1244959142" CREATED="1392118494907" MODIFIED="1392118494935" LINK="http://csp1/redmine/issues/8976"/>
<node TEXT="RM8977 - [Review] GH44 - Controllers for icons" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1114166604" CREATED="1392118501617" MODIFIED="1392118501621" LINK="http://csp1/redmine/issues/8977"/>
</node>
</node>
<node TEXT="Decorators" STYLE_REF="Validated (Green)" ID="ID_1842613657" CREATED="1392110833130" MODIFIED="1393666915045">
<node TEXT="Static" ID="ID_1357135828" CREATED="1392110846744" MODIFIED="1392110850513"/>
<node TEXT="Final" ID="ID_1908431052" CREATED="1392110850773" MODIFIED="1392110852193"/>
<node TEXT="Abstract" ID="ID_1945760035" CREATED="1392110852480" MODIFIED="1392110854911"/>
<node TEXT="Volatile" ID="ID_500348594" CREATED="1392135963177" MODIFIED="1392135977943"/>
<node TEXT="Native" ID="ID_1241072645" CREATED="1392135978524" MODIFIED="1392135983186"/>
<node TEXT="Transient" ID="ID_1021811390" CREATED="1392135984654" MODIFIED="1392135987008"/>
</node>
</node>
<node TEXT="Add Child Descriptors" STYLE_REF="Validated (Green)" ID="ID_1032913597" CREATED="1392111239862" MODIFIED="1393666920834"/>
</node>
<node TEXT="GH57 - Issues with javadoc sync, because we use the rewriter in modifying mode (descriptive mode hasn&apos;t got this issue)" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_382931421" CREATED="1392303857819" MODIFIED="1392303983664" LINK="https://github.com/flower-platform/flower-platform-4/issues/57">
<node TEXT="There is a deactivated test, because of javadoc and annotations issues: testMatchNoConflictsAndPerformSync." ID="ID_1677341359" CREATED="1394550725649" MODIFIED="1394550991105"/>
</node>
</node>
<node TEXT="Help" POSITION="left" ID="ID_490517294" CREATED="1327566761646" MODIFIED="1375880809295" LINK="FAP%20-%20Help.mm"/>
<node TEXT="metadata" FOLDED="true" POSITION="left" ID="ID_1036783391" CREATED="1389695877011" MODIFIED="1389778344551">
<font SIZE="8" ITALIC="true"/>
<node TEXT="gitHubUser" ID="ID_436277226" CREATED="1389697906198" MODIFIED="1389697906198">
<node TEXT="flower-platform" ID="ID_1118684663" CREATED="1389697909896" MODIFIED="1389698732522"/>
</node>
<node TEXT="gitHubRepo" ID="ID_851499351" CREATED="1389697911736" MODIFIED="1389697925248">
<node TEXT="flower-platform-4" ID="ID_1136187539" CREATED="1389697926616" MODIFIED="1389699222396"/>
</node>
<node TEXT="gitHubAuth" ID="ID_733726960" CREATED="1389697928626" MODIFIED="1389697931258">
<node TEXT="b0de019eca8a1914d7bd4295f786a421153d41de" ID="ID_1151666958" CREATED="1389697932806" MODIFIED="1389698747042"/>
</node>
<node TEXT="redmineHost" ID="ID_1121707926" CREATED="1389697906198" MODIFIED="1389778654129" VSHIFT="40">
<node TEXT="http://csp1/redmine" ID="ID_1852927176" CREATED="1389697909896" MODIFIED="1389778401431"/>
</node>
<node TEXT="redmineProject" ID="ID_329898888" CREATED="1389697911736" MODIFIED="1389778413821">
<node TEXT="flower-platform-4" ID="ID_1406277967" CREATED="1389697926616" MODIFIED="1389778880184"/>
</node>
<node TEXT="apiKey" ID="ID_1361848847" CREATED="1389697928626" MODIFIED="1390589964670">
<node TEXT="10bf7555dbca38c5f07a808464d3f51f818fcf07" ID="ID_1329109619" CREATED="1389781581252" MODIFIED="1389781581252"/>
</node>
<node TEXT="apiKey" ID="ID_180191296" CREATED="1390589526855" MODIFIED="1390589529313">
<node TEXT="9abc7ff232395b4fe8d784af4f60273a250460de" ID="ID_1654830149" CREATED="1390589538523" MODIFIED="1390589538523"/>
</node>
<node TEXT="duplicateGitHubIssuePrefix" ID="ID_101126721" CREATED="1389780071450" MODIFIED="1389780071450">
<node TEXT="[Review]" ID="ID_837556558" CREATED="1389780073449" MODIFIED="1389780077490"/>
</node>
</node>
<node TEXT="Construction site" POSITION="left" ID="ID_1605951912" CREATED="1389423489709" MODIFIED="1393273610509">
<icon BUILTIN="mindmap"/>
<node TEXT="Reflectii functionalitati" FOLDED="true" ID="ID_797170355" CREATED="1393269921476" MODIFIED="1393273654700">
<icon BUILTIN="narrative"/>
<node TEXT="Integrarea cu sisteme de cod review, in special GitHub" FOLDED="true" ID="ID_482451912" CREATED="1393269932578" MODIFIED="1395233597828">
<icon BUILTIN="narrative"/>
<node TEXT="Discuss My Design" ID="ID_300542085" CREATED="1393269960857" MODIFIED="1393270037167">
<font BOLD="true"/>
</node>
<node TEXT="Un design inseamna" ID="ID_305951898" CREATED="1393270038169" MODIFIED="1393270064200">
<node TEXT="Niste modificari planificate. Eventual comentate" ID="ID_1760653255" CREATED="1393270065445" MODIFIED="1393270084580"/>
<node TEXT="Un pull request sau patch pe care vreau sa il integrez. Diff colorat, high level, raportat la design initial" ID="ID_714754453" CREATED="1393270085688" MODIFIED="1393270140246"/>
<node TEXT="Optional cateva iteratii aditionale" ID="ID_1525587235" CREATED="1393270147404" MODIFIED="1393270155170"/>
</node>
<node TEXT="La sfarsitul unui design = review de cod" ID="ID_1314874748" CREATED="1393270211181" MODIFIED="1395233588770"/>
<node TEXT="Cum am putea integra/parcurge impreuna design + review?" ID="ID_1406990766" CREATED="1393270290861" MODIFIED="1393270309941">
<font BOLD="true"/>
</node>
<node TEXT="2 abordari" ID="ID_1440330818" CREATED="1393270239164" MODIFIED="1393270249810">
<node TEXT="Parcurg liniar review" ID="ID_102396764" CREATED="1393270270776" MODIFIED="1393270368084">
<node TEXT="Sigur trec astfel prin toate modificarile" ID="ID_1973953043" CREATED="1393270582998" MODIFIED="1393270743613"/>
<node TEXT="Lung. Cateodata, de plictiseala sunt tentat sa dau repede si pe zone importante" ID="ID_430074656" CREATED="1393270744135" MODIFIED="1393270766461"/>
</node>
<node TEXT="Parcurg design, si sar la diverse fisiere din review" ID="ID_1605020629" CREATED="1393270432436" MODIFIED="1393270465787">
<node TEXT="Pot sa ma concentrez mai usor pe zonele importante." ID="ID_1521615839" CREATED="1393270777290" MODIFIED="1393270790140"/>
<node TEXT="Dar tot deschizand taburi noi, cred ca se pierde mai mult timp" ID="ID_826461114" CREATED="1393270790504" MODIFIED="1393270802842"/>
</node>
</node>
</node>
<node TEXT="Discutii despre repo" FOLDED="true" ID="ID_147018523" CREATED="1393357163861" MODIFIED="1395233597825">
<icon BUILTIN="narrative"/>
<node TEXT="Desktop/IDE" ID="ID_55573115" CREATED="1393357176053" MODIFIED="1393357183308">
<node TEXT="Un repo = un anumit subset de proiecte, din ws" ID="ID_1270529773" CREATED="1393357188330" MODIFIED="1393357220670"/>
<node TEXT="Preferabil, acel subset = de fapt cu toate proiectele dintr-un git repo" ID="ID_795877188" CREATED="1393357220925" MODIFIED="1393357252252"/>
<node TEXT="E.g." ID="ID_1772943704" CREATED="1393357254707" MODIFIED="1393357256503">
<node TEXT="workspace" ID="ID_1302992067" CREATED="1393357257648" MODIFIED="1393357263281">
<node TEXT="proj1" STYLE_REF="Done (Blue)" ID="ID_951119692" CREATED="1393357264427" MODIFIED="1393357471721"/>
<node TEXT="proj2" STYLE_REF="Done (Blue)" ID="ID_799046792" CREATED="1393357267212" MODIFIED="1393357471737"/>
<node TEXT="proj-fp-repo (svn)" STYLE_REF="Done (Blue)" ID="ID_128744934" CREATED="1393357444091" MODIFIED="1393357475364">
<node TEXT="recomandat de comis la acelasi nivel pe SVN" ID="ID_768540037" CREATED="1393357523597" MODIFIED="1393357540728"/>
</node>
<node TEXT="proj3" ID="ID_150555190" CREATED="1393357274839" MODIFIED="1393357327272">
<font ITALIC="true"/>
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_1923333487" STARTINCLINATION="53;0;" ENDINCLINATION="53;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
<node TEXT="proj4" ID="ID_722227025" CREATED="1393357277890" MODIFIED="1393357331615">
<font ITALIC="true"/>
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_913673408" STARTINCLINATION="53;0;" ENDINCLINATION="53;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
<node TEXT="proj5" ID="ID_1016051906" CREATED="1393357487319" MODIFIED="1393357492079"/>
<node TEXT="proj6" ID="ID_48844520" CREATED="1393357492397" MODIFIED="1393357494739"/>
</node>
<node TEXT="git-repo" ID="ID_1484067395" CREATED="1393357270370" MODIFIED="1393357273819">
<node TEXT=".git" ID="ID_957437806" CREATED="1393357660781" MODIFIED="1393357664174">
<font ITALIC="true"/>
</node>
<node TEXT="proj3" STYLE_REF="In progress (Yellow)" ID="ID_1923333487" CREATED="1393357274839" MODIFIED="1393357506727"/>
<node TEXT="proj4" STYLE_REF="In progress (Yellow)" ID="ID_913673408" CREATED="1393357277890" MODIFIED="1393357506727"/>
<node TEXT="proj-fp-repo (git)" STYLE_REF="In progress (Yellow)" ID="ID_1990813818" CREATED="1393357281219" MODIFIED="1393357506743">
<node TEXT=".xml" ID="ID_199542496" CREATED="1393357293145" MODIFIED="1393357295191">
<node TEXT="nume repo?" STYLE_REF="New (Red)" ID="ID_1273503456" CREATED="1393358035738" MODIFIED="1393358207191">
<node TEXT="legam de numele proj de config? sau il lasam liber?" ID="ID_1709057738" CREATED="1393358278699" MODIFIED="1393358295752"/>
</node>
<node TEXT="proj3" ID="ID_1143340464" CREATED="1393357296368" MODIFIED="1393357298755"/>
<node TEXT="proj4" ID="ID_1296760192" CREATED="1393357298933" MODIFIED="1393357301649"/>
<node TEXT="..." ID="ID_544370765" CREATED="1393358032609" MODIFIED="1393358033469"/>
</node>
</node>
</node>
</node>
<node TEXT="Operatiuni" ID="ID_1869455006" CREATED="1393357553752" MODIFIED="1393357763977">
<font BOLD="true"/>
<node ID="ID_1423985218" CREATED="1393357558595" MODIFIED="1393357610325"><richcontent TYPE="NODE">

<html>
  <head>
    
  </head>
  <body>
    <p>
      <b>Practic, un repo NU se creaza; </b>
    </p>
    <p>
      el se configureaza, pornind de la acel subset
    </p>
  </body>
</html>
</richcontent>
</node>
<node ID="ID_1186199192" CREATED="1393358749456" MODIFIED="1393358904438"><richcontent TYPE="NODE">

<html>
  <head>
    
  </head>
  <body>
    <p>
      Lucrez cu un ws eclipse. <b>NU am</b>&#160;un &quot;repo root&quot;
    </p>
  </body>
</html>
</richcontent>
</node>
</node>
</node>
<node TEXT="Server" ID="ID_903908765" CREATED="1393357183564" MODIFIED="1393357185578">
<node TEXT="Exista o zona de date; un fel de workspace" ID="ID_809460020" CREATED="1393357345843" MODIFIED="1393357380570"/>
<node TEXT="Un user sau o organizatie are directoare pe primul nivel" ID="ID_1074665148" CREATED="1393357391449" MODIFIED="1393357412909"/>
<node TEXT="In dir unui user/org avem diverse repo" ID="ID_1785767654" CREATED="1393357414927" MODIFIED="1393357433899"/>
<node TEXT="E.g." ID="ID_757054115" CREATED="1393357630073" MODIFIED="1393357632976">
<node TEXT="user1" ID="ID_1356722442" CREATED="1393357636008" MODIFIED="1393357641143"/>
<node TEXT="org2" ID="ID_911667405" CREATED="1393357641538" MODIFIED="1393357645503"/>
<node TEXT="user3" ID="ID_1354763201" CREATED="1393357646039" MODIFIED="1393357649364">
<node TEXT="repo1" ID="ID_393654582" CREATED="1393357686872" MODIFIED="1393357714338">
<node TEXT="proj1" STYLE_REF="Done (Blue)" ID="ID_1324021619" CREATED="1393357701310" MODIFIED="1393357730710"/>
<node TEXT="proj2" STYLE_REF="Done (Blue)" ID="ID_1057103871" CREATED="1393357707386" MODIFIED="1393357730710"/>
</node>
<node TEXT="repo2" ID="ID_385691687" CREATED="1393357650976" MODIFIED="1393357718113">
<node TEXT=".git" ID="ID_351305664" CREATED="1393357669307" MODIFIED="1393357673667">
<font ITALIC="true"/>
</node>
<node TEXT="proj5" STYLE_REF="In progress (Yellow)" ID="ID_1383756557" CREATED="1393357674330" MODIFIED="1393357735445"/>
<node TEXT="proj6" STYLE_REF="In progress (Yellow)" ID="ID_391545426" CREATED="1393357678191" MODIFIED="1393357735445"/>
<node TEXT="proj-fp-repo" STYLE_REF="Planned (Orange)" ID="ID_1859868289" CREATED="1393358362027" MODIFIED="1393358372746">
<node TEXT="Deja exista 2 nume de repo: unul de aici si unul cf. dir; pe care il utilizam?" STYLE_REF="New (Red)" ID="ID_1222269280" CREATED="1393358377245" MODIFIED="1393358431667"/>
<node TEXT="Serverul nu are practic nevoie de acest proiect; el ar putea foarte bine stoca in BD. Este totusi util, pentru ca se poate importa repede pe server config de pe IDE, si vice versa. Ar avea sens sistemul de resursa virtuala" STYLE_REF="New (Red)" ID="ID_900018558" CREATED="1393358434934" MODIFIED="1393358522889"/>
</node>
</node>
</node>
</node>
<node TEXT="Operatiuni" ID="ID_174679564" CREATED="1393357780363" MODIFIED="1393357783756">
<font BOLD="true"/>
<node ID="ID_1248398671" CREATED="1393357784216" MODIFIED="1393358216683"><richcontent TYPE="NODE">

<html>
  <head>
    
  </head>
  <body>
    <p>
      Creerea unui repo inseamna de fapt <b>creerea unui dir</b>
    </p>
  </body>
</html>
</richcontent>
</node>
<node TEXT="Are sens nodul de file system la nivel de repo" ID="ID_496931232" CREATED="1393357819745" MODIFIED="1393357829856"/>
<node ID="ID_894918179" CREATED="1393358765860" MODIFIED="1393358920638"><richcontent TYPE="NODE">

<html>
  <head>
    
  </head>
  <body>
    <p>
      Nu lucrez cu ws. <b>AM </b>un &quot;repo root&quot;
    </p>
  </body>
</html>
</richcontent>
</node>
</node>
</node>
</node>
<node TEXT="Scalare" FOLDED="true" ID="ID_609292404" CREATED="1395297648535" MODIFIED="1395297653017">
<node TEXT="Geografie" ID="ID_1132946009" CREATED="1395297654245" MODIFIED="1395297656964">
<node TEXT="As vrea sa pornesc cu un server, hostat la noi" ID="ID_855161202" CREATED="1395297521935" MODIFIED="1395297598581"/>
<node TEXT="Apoi sa maresc numarul de servere, hostate tot la noi" ID="ID_1383895191" CREATED="1395297607314" MODIFIED="1395297622934"/>
<node TEXT="Apoi as dori sa ne extindem si intr-un alt data center" ID="ID_1212557563" CREATED="1395297629563" MODIFIED="1395297646400"/>
</node>
<node TEXT="Flow" ID="ID_204039496" CREATED="1395297753039" MODIFIED="1395297754521">
<node TEXT="In teorie, aplicatia noastra este destul de stateless. Daca am avea mai multe servere, atunci cerinte consecutive ale unui client, ar putea fi servite de oricare din servere. Ar trebui replicata sesiunea intre servere, care nu inseamna mare lucru (user logat si cam atat)." ID="ID_1995304275" CREATED="1395297755416" MODIFIED="1395297839104"/>
<node TEXT="Dar daca cerinte consecutive sunt executate de servere diferite (care ar avea BD diferite), inseamna ca datele trebuie sa fi ajuns din BD1 in BD2 intre cereri. La o geografie locala (acelasi data center), nu e insurmontabil, insa la data centere diferite, cred ca e o problema.&#xa;&#xa;Mai ales ca am impresia ca sincronizarea e asincrona. I.e. cand ma duc pe server 2: daca nu au sosit datele, eu nu stiu, si deci pot sa cer data care sunt vechi/inconsistente (e.g. desi am adaugat un nod, nu il vad)." ID="ID_177553914" CREATED="1395297852517" MODIFIED="1395298008811"/>
<node TEXT="Partitionare/sharding" ID="ID_1624870019" CREATED="1395298083011" MODIFIED="1395298088815">
<node TEXT="Am putea sa partitionam datele la nivel de repo." ID="ID_127599245" CREATED="1395298089307" MODIFIED="1395298113725"/>
<node TEXT="Astfel, stim ca repo5 apartine de server 2. Deci load balancerul trimite toate cererile catre repo5, catre server 2. Asta combinat totusi cu un sistem de replicare. I.e. daca server2 pica, totusi si celelalte server au datele, si server 3 e.g. preia releul" ID="ID_1884438670" CREATED="1395298114097" MODIFIED="1395298202965"/>
<node TEXT="E bine sa stim in ce shard este un row, fara sa facem query in BD. Parca vazusem ceva de genul &quot;HASH pe nume de repo, si apoi MOD la cate shard logice avem&quot;. =&gt; acest repo e pe shard 3." ID="ID_1958161473" CREATED="1395315602382" MODIFIED="1395315745903"/>
<node TEXT="Utilizare shard logic/fizic. Cresterea de nr de shard dinamic." ID="ID_253671398" CREATED="1395315830805" MODIFIED="1395315864325"/>
</node>
</node>
<node TEXT="Neclaritati" ID="ID_488943438" CREATED="1395315747825" MODIFIED="1395315750349">
<node TEXT="Se tot mentioneaza de HA. Adica datele de pe un shard, si replicate in alta parte. Aceasta alta parte ce este de fapt: un alt server care la randul lui e si el un shard? Sau un server care sta in expectativa, si nu e folosit decat in caz de pb?" ID="ID_1398482759" CREATED="1395315751754" MODIFIED="1395315936464"/>
</node>
<node TEXT="Link-uri:" ID="ID_1209667482" CREATED="1395315158600" MODIFIED="1395315160790">
<node TEXT="Spre sfarsit, niste comparatie de framework-uri de sharding" ID="ID_1712186354" CREATED="1395315165010" MODIFIED="1395315257000" LINK="http://www.cubrid.org/blog/cubrid-life/database-sharding-with-cubrid/"/>
<node TEXT="http://www.craigkerstiens.com/2012/11/30/sharding-your-database/" ID="ID_996700022" CREATED="1395315815083" MODIFIED="1395315815083" LINK="http://www.craigkerstiens.com/2012/11/30/sharding-your-database/"/>
</node>
</node>
</node>
<node TEXT="Sincronizare&#xa;24/02/2014" ID="ID_976798196" CREATED="1393273662169" MODIFIED="1393273664073">
<icon BUILTIN="group"/>
<node TEXT="Next steps:" ID="ID_802459546" CREATED="1393273667653" MODIFIED="1393273682578"/>
<node TEXT="Sistemul de agregare de info pe server, bazat pe tree-uri de fisiere" ID="ID_861068064" CREATED="1393273683045" MODIFIED="1393273825156"/>
<node TEXT="interfata codesync cu code-rev/git-hub" ID="ID_489805131" CREATED="1393273825513" MODIFIED="1393273842637"/>
<node TEXT="ui de diff bazat pe orion" ID="ID_1515701988" CREATED="1393273842970" MODIFIED="1393273909728"/>
</node>
<node TEXT="Sincronizare&#xa;20/03/2014" ID="ID_1162063756" CREATED="1395317699533" MODIFIED="1395317701238">
<icon BUILTIN="group"/>
<node TEXT="Repos" ID="ID_1106281034" CREATED="1395342497418" MODIFIED="1395342499530">
<node TEXT="Compatibil web &amp; desktop" ID="ID_965643491" CREATED="1395342502088" MODIFIED="1395342513190"/>
</node>
<node TEXT="Database storage" ID="ID_1659181808" CREATED="1395330587976" MODIFIED="1396462348813">
<icon BUILTIN="narrative"/>
<node TEXT="On first access =&gt; import into DB" FOLDED="true" ID="ID_471638920" CREATED="1395330597426" MODIFIED="1396462348813">
<icon BUILTIN="narrative"/>
<node TEXT="category.resourceContent.*" ID="ID_812035423" CREATED="1396075198206" MODIFIED="1396075766233"/>
<node TEXT="category.resource.*" ID="ID_1134162762" CREATED="1396075766568" MODIFIED="1396075773355"/>
<node TEXT="where * =" ID="ID_1348908723" CREATED="1396075773970" MODIFIED="1396075778900">
<node TEXT="freePlanePersistence" ID="ID_1456573478" CREATED="1396075779390" MODIFIED="1396075786365"/>
<node TEXT="freePlaneMindMap" ID="ID_58275806" CREATED="1396075786574" MODIFIED="1396075793611"/>
<node TEXT="emfPersistence" ID="ID_466551516" CREATED="1396075793913" MODIFIED="1396075797675"/>
</node>
<node TEXT="category.resource.general" ID="ID_1298447615" CREATED="1396075821105" MODIFIED="1396075828345">
<node TEXT="CorePlugin.defaultResourcePersistenceCategory" STYLE_REF="Code" ID="ID_1388144721" CREATED="1396075829583" MODIFIED="1396075860356">
<node TEXT="venita prin proprietate" STYLE_REF="Resubmitted (Violet)" ID="ID_541490296" CREATED="1396462145430" MODIFIED="1396462156991"/>
<node TEXT="e.g. emfResource" STYLE_REF="Resubmitted (Violet)" ID="ID_354668251" CREATED="1396462149950" MODIFIED="1396462157001"/>
</node>
<node TEXT="pluginurile, dc default.. = ei =&gt; isi inregistreaza ei ce au in category.resource.*" ID="ID_320265253" CREATED="1396075865714" MODIFIED="1396075889739"/>
<node TEXT="e.g. code will have this category" ID="ID_229493769" CREATED="1396076161442" MODIFIED="1396076179759"/>
</node>
<node TEXT="category.resource.database" ID="ID_776068358" CREATED="1396076297858" MODIFIED="1396076307781">
<node TEXT="Database plugin, will register on this category, controllers with lower index" ID="ID_855786665" CREATED="1396076333622" MODIFIED="1396076421467">
<node TEXT="ResourceAccessController" ID="ID_928119398" CREATED="1396076421987" MODIFIED="1396076427512"/>
<node TEXT="ChildrenProvider" ID="ID_634881780" CREATED="1396076428765" MODIFIED="1396076436053"/>
<node TEXT="RawNodeDataProvider: NullController" ID="ID_1650090698" CREATED="1396076436761" MODIFIED="1396076447339"/>
</node>
</node>
<node TEXT="nodes of type resource:" ID="ID_694883465" CREATED="1396076459139" MODIFIED="1396076473071">
<node TEXT="e.g. code has categories:" ID="ID_202669694" CREATED="1396076161442" MODIFIED="1396076501430">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_1298447615" STARTINCLINATION="196;0;" ENDINCLINATION="196;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_776068358" STARTINCLINATION="147;0;" ENDINCLINATION="147;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
<node TEXT="registers prop provider" ID="ID_1924043447" CREATED="1396076567122" MODIFIED="1396076630944">
<node TEXT="says: isResource" ID="ID_409407987" CREATED="1396076631840" MODIFIED="1396076637098"/>
<node TEXT="says: resource type. If nothing, =&gt; assumed &quot;general&quot;. Else, it can say: .freePlanePersistence&#xa;Hint: we can use a prop provider with default value" ID="ID_180112301" CREATED="1396076637712" MODIFIED="1396076683890"/>
</node>
</node>
<node TEXT="DataBaseResourceAccessController&#xa;(maybe ChildrenProvider also?)" ID="ID_1673210654" CREATED="1396461623434" MODIFIED="1396461641645">
<node TEXT="looks at &quot;resourceType&quot;. E.g. general or emfPersistence" ID="ID_7778628" CREATED="1396461642204" MODIFIED="1396461683005"/>
<node TEXT="Instantiates a new NodeService, ResourceService, TypeDescriptorRegistry" ID="ID_1057449452" CREATED="1396461683484" MODIFIED="1396461699885">
<node TEXT="deci trebuie sa nu mai folosim pe nicaieri instanta de serviciu" STYLE_REF="Resubmitted (Violet)" ID="ID_1584517362" CREATED="1396462291621" MODIFIED="1396462314962"/>
</node>
<node TEXT="copies: dc ! general: category.resourceContent.VALUE, category.resource.VALUE&#xa;else:  category.resourceContent.CORE_PLUGIN.DEFAULT..., category.resource.general" ID="ID_725627783" CREATED="1396461709794" MODIFIED="1396462265652"/>
</node>
<node TEXT="?" ID="ID_1253529597" CREATED="1396075937870" MODIFIED="1396075939651">
<node TEXT="cum facem trecerea de la repo -&gt; code, mda, etc?" ID="ID_1604571065" CREATED="1396076061563" MODIFIED="1396076073172"/>
<node TEXT=".db: pt file?" ID="ID_161388668" CREATED="1396076323036" MODIFIED="1396076326517"/>
<node TEXT="Eu am niste OFF-uri. Suspectez ca ceva nu e bun in intreaga structura de noduri (care cara dupa ele mereu tip si resursa):" ID="ID_1443778465" CREATED="1397566121394" MODIFIED="1397566206537">
<node TEXT="Pe sistemul file, fac in RawNodeDataProvider test ca tipul de pe client e de fapt tipul din fisier." ID="ID_893345990" CREATED="1397566208595" MODIFIED="1397566245757"/>
<node TEXT="In varianta DB, voi trambala mereu dupa mine informatia: tip, resursa, desi doar ID-ul identifica unic. Si practic bazandu-ma pe el, as putea vedea tipul si resursa.&#xa;Totusi ce ne facem cu nodurile de tip &quot;file&quot;, &quot;repo&quot;? Pentru ele, pe langa id, am nevoie de un tip&#xa;Poate nu ar fi fost asa de rau sa stim direct pentru un nod: tip resursa, cale resursa, id. Iar tipul nodului il deduc pe baza astora.&#xa;Si cu nodurile &quot;virtuale&quot;?" ID="ID_493923492" CREATED="1397566248345" MODIFIED="1397566414187"/>
</node>
</node>
</node>
<node TEXT="Correspondence: repo + path -&gt; db id of root node. Serves also as a flag. Maybe also remember the timestamp of the inserted file." ID="ID_337568587" CREATED="1395330677749" MODIFIED="1395330782852"/>
<node TEXT="On file access =&gt; write from DB to file" ID="ID_1776407827" CREATED="1395330783070" MODIFIED="1395330799762">
<node TEXT="Which points?" ID="ID_908755791" CREATED="1395330800590" MODIFIED="1395330804162"/>
<node TEXT="File export" ID="ID_639151770" CREATED="1395330804440" MODIFIED="1395330806652"/>
<node TEXT="Git commit" ID="ID_17047834" CREATED="1395330806930" MODIFIED="1395330809062"/>
</node>
<node TEXT="On file modified =&gt; reimport into BD" ID="ID_1455166678" CREATED="1395330811970" MODIFIED="1395330827993"/>
<node TEXT="Import: in fact merge" ID="ID_290801630" CREATED="1395330828351" MODIFIED="1395330877333"/>
<node TEXT="Merge dialog based on codesync. Store results into DB, for stateless conflict solving" ID="ID_311234043" CREATED="1395330879181" MODIFIED="1395330898473"/>
<node TEXT="Refactor the resource field, to optimize for partitioning/sharding" ID="ID_1392059451" CREATED="1395330913032" MODIFIED="1395330949924"/>
</node>
<node TEXT="Link-uri" FOLDED="true" ID="ID_1497729800" CREATED="1395823669457" MODIFIED="1395910655607">
<icon BUILTIN="narrative"/>
<richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      analizat
    </p>
  </body>
</html>
</richcontent>
<node TEXT="idee initiala" ID="ID_1495910190" CREATED="1395863386941" MODIFIED="1395863393282">
<node TEXT="nod1" ID="ID_1499377004" CREATED="1395823677303" MODIFIED="1395826056119"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      (type|res|1234)
    </p>
  </body>
</html>
</richcontent>
<node TEXT="Links/in" ID="ID_381530279" CREATED="1395825476708" MODIFIED="1395825490075"/>
<node TEXT="Links/out" ID="ID_1587943238" CREATED="1395825490344" MODIFIED="1395825492623">
<node TEXT="(type|res|1233)" ID="ID_1822982184" CREATED="1395825539232" MODIFIED="1395826842974">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_854830664" STARTINCLINATION="95;0;" ENDINCLINATION="95;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
</node>
</node>
<node TEXT="nod2" ID="ID_1277707185" CREATED="1395825473706" MODIFIED="1395826045796"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      (type|res|1233)
    </p>
  </body>
</html>
</richcontent>
<node TEXT="Links/in" ID="ID_1817769187" CREATED="1395825476708" MODIFIED="1395826226207"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      (inLinks||(type|res|1233)
    </p>
  </body>
</html>
</richcontent>
<node TEXT="(type|res|1234)" ID="ID_854830664" CREATED="1395825519521" MODIFIED="1395826073974"/>
</node>
<node TEXT="Links/out" ID="ID_1027995695" CREATED="1395825490344" MODIFIED="1395826308262"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      (outLinks|res|@type,1233)
    </p>
  </body>
</html>
</richcontent>
</node>
</node>
</node>
<node TEXT="Persistenta" STYLE_REF="In progress (Yellow)" ID="ID_1843334476" CREATED="1395908557487" MODIFIED="1395908564610">
<node TEXT="MDA, Diagrame" ID="ID_1477444076" CREATED="1395908587809" MODIFIED="1395908594290">
<node TEXT="client" ID="ID_892299145" CREATED="1395908595038" MODIFIED="1395908603830"/>
<node TEXT="server" ID="ID_1608995781" CREATED="1395908604128" MODIFIED="1395908605740"/>
<node TEXT="shareable" ID="ID_572868135" CREATED="1395908608178" MODIFIED="1395908612520"/>
<node TEXT="versionabil" ID="ID_756547421" CREATED="1395908617058" MODIFIED="1395908621540"/>
</node>
<node TEXT="CodeSync" ID="ID_24828923" CREATED="1395908625128" MODIFIED="1395908628520">
<node TEXT="client" ID="ID_271249242" CREATED="1395908629628" MODIFIED="1395908630990"/>
<node TEXT="server" ID="ID_1543005140" CREATED="1395908631208" MODIFIED="1395908632450"/>
<node TEXT="non-shareable" ID="ID_1616059730" CREATED="1395908632838" MODIFIED="1395908635190"/>
<node TEXT="non-versionabil" ID="ID_1832101795" CREATED="1395908635408" MODIFIED="1395908638220"/>
</node>
<node TEXT="DataSunc" ID="ID_789818824" CREATED="1395908650648" MODIFIED="1395908652820">
<node TEXT="server only" ID="ID_1035849927" CREATED="1395908653538" MODIFIED="1395908663240"/>
</node>
</node>
<node TEXT="Legenda:" ID="ID_1781349051" CREATED="1395908880719" MODIFIED="1395908884790">
<node TEXT="sursa (in principiu &quot;important&quot;)" STYLE_REF="Done (Blue)" ID="ID_399918317" CREATED="1395908715278" MODIFIED="1395908873200"/>
<node TEXT="destinatie (in principiu &quot;!important&quot;)" STYLE_REF="New (Red)" ID="ID_1876511833" CREATED="1395908850638" MODIFIED="1395908871930"/>
</node>
<node TEXT="MDA -&gt; CodeSync" ID="ID_320056221" CREATED="1395863398571" MODIFIED="1395908081213">
<icon BUILTIN="full-1"/>
<node TEXT="E.g." ID="ID_1362843041" CREATED="1395907072262" MODIFIED="1395907073934">
<node TEXT="MyEntity (mda)" ID="ID_555035277" CREATED="1395863424491" MODIFIED="1395863638254">
<node TEXT="Links" ID="ID_427136496" CREATED="1395864059414" MODIFIED="1395864061735">
<node TEXT="entity (MyEntity.java)" STYLE_REF="Done (Blue)" ID="ID_1171341763" CREATED="1395863433651" MODIFIED="1395907587018" LINK="#ID_603632096"/>
<node TEXT="service (MyEntityService.java)" ID="ID_293089678" CREATED="1395863433651" MODIFIED="1395900170157" LINK="1"/>
</node>
</node>
<node TEXT="MyEntity.java (cs)" ID="ID_1680138360" CREATED="1395863620151" MODIFIED="1395907541178">
<node TEXT="entity (MyEntity)" STYLE_REF="New (Red)" ID="ID_603632096" CREATED="1395863433651" MODIFIED="1395907589948" LINK="#ID_1171341763"/>
</node>
</node>
<node TEXT="Grupare/titlu" ID="ID_383806909" CREATED="1395908973958" MODIFIED="1395908978008">
<font BOLD="true"/>
<node TEXT="Grupare intr-un &quot;links&quot;, si pus tipul de link in eticheta link" ID="ID_327290636" CREATED="1395863495841" MODIFIED="1395864096885"/>
</node>
<node TEXT="Folosire" ID="ID_808229343" CREATED="1395900196753" MODIFIED="1395900510059">
<font BOLD="true"/>
<node TEXT="De la ambele capete:" ID="ID_1392809489" CREATED="1395909387012" MODIFIED="1395909392533"/>
<node TEXT="prind o operatiune pe un tip care poate avea legaturi (e.g. entitate)" ID="ID_1516409730" CREATED="1395900201487" MODIFIED="1395900239756"/>
<node TEXT="parcurg link-urile de regula: as putea sa ma uit in copiii din &quot;Wizard links&quot;" ID="ID_1586121479" CREATED="1395900216830" MODIFIED="1395900371078"/>
<node TEXT="pt fiecare am un cuplu: fullNodeId, failoverInfo. Incerc sa rezolv nodul cu prima. Dc. nu gasesc, incerc cu a 2a. Dc. nu gasesc =&gt; intru in modul de broken link." ID="ID_668132537" CREATED="1395900403423" MODIFIED="1395900464873"/>
<node TEXT="Avand cele 2 noduri =&gt; pot sa-mi execut regula" ID="ID_258176846" CREATED="1395900465254" MODIFIED="1395900487018"/>
</node>
</node>
<node TEXT="Diagrame -&gt; CodeSync, MDA, etc" ID="ID_1033409719" CREATED="1395900882593" MODIFIED="1395908091343">
<icon BUILTIN="full-2"/>
<node TEXT="e.g." ID="ID_18716846" CREATED="1395908074581" MODIFIED="1395908076513">
<node TEXT="MyClassView" ID="ID_865040725" CREATED="1395900920790" MODIFIED="1395900924006">
<node TEXT="prop: view_link: MyClassId+failover" STYLE_REF="Done (Blue)" ID="ID_1341878714" CREATED="1395900943285" MODIFIED="1395908110943" LINK="#ID_1896185521"/>
</node>
<node TEXT="MyClass" ID="ID_586728612" CREATED="1395900916180" MODIFIED="1395900920488">
<node TEXT="backlink from MyClassView" STYLE_REF="New (Red)" ID="ID_1896185521" CREATED="1395900964603" MODIFIED="1395908122583" LINK="#ID_1341878714"/>
</node>
</node>
<node TEXT="Folosire" ID="ID_649508550" CREATED="1395909581441" MODIFIED="1395909584791">
<font BOLD="true"/>
<node TEXT="Dinspre diagrama:" ID="ID_982721276" CREATED="1395909654131" MODIFIED="1395909660823"/>
<node TEXT="Obtinere nod destinatie. Fie il folosesc pe client, fie am o logica java care face o calculare de eticheta, etc." ID="ID_1294997086" CREATED="1395909625941" MODIFIED="1395909680003"/>
<node TEXT="Dinspre CS/etc:" ID="ID_290347546" CREATED="1395909680521" MODIFIED="1395909688313"/>
<node TEXT="pentru navigare catre diagrama corespunzatoare" ID="ID_1768895433" CREATED="1395909688691" MODIFIED="1395909702803"/>
</node>
</node>
<node TEXT="CodeSync &lt;-&gt; DataSync" ID="ID_1388513291" CREATED="1395863600771" MODIFIED="1395908520649">
<icon BUILTIN="full-3"/>
<node TEXT="e.g." ID="ID_36486291" CREATED="1395908234625" MODIFIED="1395908235996">
<node TEXT="myMethod" ID="ID_882866976" CREATED="1395863726792" MODIFIED="1395863730204">
<node TEXT="Comments" ID="ID_1276548300" CREATED="1395863731492" MODIFIED="1395863737444">
<node TEXT="2013-3-1 move this...." ID="ID_1108036937" CREATED="1395863748302" MODIFIED="1395908297389" LINK="#ID_1995774430"/>
</node>
<node TEXT="Issues" ID="ID_1410795812" CREATED="1395863740522" MODIFIED="1395863744914"/>
</node>
<node TEXT="comment: 2013-3-1 move this...." ID="ID_918622898" CREATED="1395908246064" MODIFIED="1395908273846">
<node TEXT="myMethod" ID="ID_1995774430" CREATED="1395908254414" MODIFIED="1395908287989" LINK="#ID_1108036937"/>
</node>
</node>
<node TEXT="Grupare/titlu" ID="ID_458771040" CREATED="1395909422331" MODIFIED="1395909425631">
<font BOLD="true"/>
<node TEXT="Grupare dupa tip de link" ID="ID_923818171" CREATED="1395863850903" MODIFIED="1395863952995"/>
</node>
<node TEXT="Folosire" ID="ID_1751701543" CREATED="1395909450851" MODIFIED="1395909453831">
<font BOLD="true"/>
<node TEXT="De la ambele capete:" ID="ID_851965521" CREATED="1395909387012" MODIFIED="1395909392533"/>
<node TEXT="Navigare din MM" ID="ID_1447359248" CREATED="1395909454491" MODIFIED="1395909602783"/>
<node TEXT="Dinspre CS/fisier: navigare din editor text, diagrama" ID="ID_701054955" CREATED="1395909603491" MODIFIED="1395909618063"/>
</node>
</node>
<node TEXT="Comun" ID="ID_1029477271" CREATED="1395904939138" MODIFIED="1395904940500">
<node TEXT="Import de resursa, scanat resursa pentru a reface capetele link-urilor" ID="ID_50919031" CREATED="1395904946648" MODIFIED="1395905038690">
<node TEXT="Deci ar fi interesant sa stocam generic o resursa; altfel algoritmul de refacere ar fi mai dificil, sau ar trebui sa punem descriptori." STYLE_REF="Resubmitted (Violet)" ID="ID_297452489" CREATED="1395905046739" MODIFIED="1395905083371"/>
</node>
<node TEXT="Parcurgere link: rezolvare in 3 etape" ID="ID_666904596" CREATED="1395905090349" MODIFIED="1395905110311">
<node TEXT="Incercat prin fullNodeId" ID="ID_154042137" CREATED="1395905111349" MODIFIED="1395905123731"/>
<node TEXT="Incercat prin string" ID="ID_1898784561" CREATED="1395905124129" MODIFIED="1395905142181">
<node TEXT="Inseamna iar ca ne trebuie un descriptor/adaptor care sa stie cum sa interpreteze acel string. Pentru CS, probabil derivat din adaptorii de model (i.e. cheia de match)." STYLE_REF="Resubmitted (Violet)" ID="ID_1631190269" CREATED="1395905142879" MODIFIED="1395905222661"/>
</node>
<node TEXT="Raportat userului &quot;broken link&quot;. Ii dam posibilitatea sa gaseasca elementul" ID="ID_1561961782" CREATED="1395905225239" MODIFIED="1395905341012"/>
</node>
<node TEXT="Stergere link-uri lazy" ID="ID_495163518" CREATED="1395905678002" MODIFIED="1395905685144">
<node TEXT="Daca sterg un element (cu link catre alt element): nu sterg capatul opus" ID="ID_536752294" CREATED="1395906098894" MODIFIED="1395906158286"/>
<node TEXT="Atunci cand ajung sa procesez capatul opus, vad ca nu este, si il sterg silent" ID="ID_1034759388" CREATED="1395906159544" MODIFIED="1395906187906">
<node TEXT="Exista notiunea de capat &quot;important&quot; si capat neimportant. Daca lipseste capatul neimportant =&gt; sterg silent. Daca lipseste cel important =&gt; zic userului sa gaseasca daca doreste (broken link).&#xa;E.g. pentru un view, capatul din element este neimportant. Pentru wiz: la fel.&#xa;Putem merge pe ideea ca capatul important este cel sursa. Daca nu ne va satisface asta in viitor =&gt; tot un sistem de descriptor.&#xa;&#xa;Pentru cazul in care fac share la un fisier de wizard, care contine link-uri neimportante catre diagrame: s-ar aplica in mod elegant algoritmul de mai sus (i.e. cand il primeste cineva care nu are acele diagrame)." STYLE_REF="Resubmitted (Violet)" ID="ID_376470513" CREATED="1395906215674" MODIFIED="1395906672077"/>
</node>
</node>
<node TEXT="UI" ID="ID_1010303991" CREATED="1395907117402" MODIFIED="1395907118034">
<node TEXT="In numele (si icon) nodului apare iconul de destinatie.&#xa;Pare a fi insa o nevoie de a include sau nu tipul de link" ID="ID_1134000747" CREATED="1395907118832" MODIFIED="1395907234301"/>
<node TEXT="Afisare link: logica comuna si cu mind-map" ID="ID_571273360" CREATED="1395909094388" MODIFIED="1395909167791"/>
</node>
<node TEXT="" ID="ID_544813734" CREATED="1395909008388" MODIFIED="1395909018850">
<icon BUILTIN="help"/>
<node TEXT="Anumite link-uri (cum sunt view): s-ar multumi doar cu o proprietate, nu cu un nod intreg" ID="ID_1613721418" CREATED="1395909022648" MODIFIED="1395909088030"/>
<node TEXT="Permitem UI generic de adaugare link-uri? Pentru ca sa avem bi-dir, ambele trebuie sa fie de tip resursa generica. Altfel, generic -&gt; non-generic (e.g. file, MM), ar fi uni-directional. Iar invers: nu putem, caci nu avem unde sa stocam info.&#xa;&#xa;Poate si descriptor/controller de link-uri?" ID="ID_1971262122" CREATED="1395909171649" MODIFIED="1395909307373"/>
<node TEXT="Navigare" ID="ID_1827237596" CREATED="1395909483831" MODIFIED="1395909485793">
<node TEXT="Trebuie deci un mecanism sa cautam/deschidem un nod arbitrar:&#xa;a) in editor curent (in principiu dc. resursa generica)&#xa;b) in editor nou (in principiu dc. e.g. MM); poate optiune" STYLE_REF="Resubmitted (Violet)" ID="ID_626345119" CREATED="1395909487771" MODIFIED="1395909538223"/>
</node>
</node>
</node>
<node TEXT="Mind Map" ID="ID_606920357" CREATED="1395863960224" MODIFIED="1395863963345">
<node TEXT="Pentru persistenta de tip &quot;fisier&quot;, nu avem nevoie de chestiile de mai sus" ID="ID_194100145" CREATED="1395864371950" MODIFIED="1395909814383"/>
<node TEXT="Insa, pentru persistenta generica (BD) am nevoie de ceva. Deci ar fi interesant un sistem de virtualizare" ID="ID_1764477027" CREATED="1395909814761" MODIFIED="1395909873524"/>
</node>
</node>
<node TEXT="Scaling/sharding" FOLDED="true" ID="ID_1015589974" CREATED="1395330973522" MODIFIED="1395330976854">
<node TEXT="Partition based on repo" ID="ID_1600277746" CREATED="1395330980292" MODIFIED="1395330988004"/>
<node TEXT="How to do it with f/s? Hadoop?" ID="ID_404878644" CREATED="1395330999512" MODIFIED="1395331007804"/>
</node>
</node>
<node TEXT="Shortcuts and preferences" STYLE_REF="In progress (Yellow)" ID="ID_1063600469" CREATED="1397552189970" MODIFIED="1397552586852">
<icon BUILTIN="mindmap"/>
<node TEXT="auto selectare element; poate chiar la nivel de diagrama" ID="ID_845251219" CREATED="1397479666531" MODIFIED="1397479765624"/>
<node TEXT="mindmap editor: propune un actionprovider" ID="ID_874623456" CREATED="1397479544121" MODIFIED="1397479552863"/>
<node TEXT="butoane de stanga, dreapta; care se fac implicit bind pe sageti" ID="ID_1674615084" CREATED="1397479689051" MODIFIED="1397479701253"/>
<node TEXT="idem si F2, facut tot prin sistemul asta" ID="ID_1438716702" CREATED="1397649665876" MODIFIED="1397649682797"/>
<node TEXT="Sistemul existent de shortcut" ID="ID_81045712" CREATED="1397547928710" MODIFIED="1397547939581">
<node TEXT="Nu cred ca trebuie sters un shortcut daca avem overlapping? In registerBinding(); de intrebat florin" ID="ID_538261378" CREATED="1397547942230" MODIFIED="1397548057700"/>
<node TEXT="avem blocare doar la ctrl; ar trebui si celelalte. Insa ce ne facem cu sagetile? Ar trebui inregistrate niste shortcuturi care sa permita iterarea" ID="ID_1895968729" CREATED="1397548064438" MODIFIED="1397548095790">
<node TEXT="filterShortcuts" STYLE_REF="Code" ID="ID_707704528" CREATED="1397548450211" MODIFIED="1397548589563"/>
<node TEXT="Noi am avea: 2 intrari: 1 pt ctrl, 1 pt alt" ID="ID_1873725434" CREATED="1397548620371" MODIFIED="1397548637803"/>
<node TEXT="+ 4 pentru sageti" ID="ID_1088777011" CREATED="1397548639461" MODIFIED="1397548651103"/>
</node>
<node TEXT="canRun: sa intrebe si actiunea" ID="ID_718487298" CREATED="1397548179960" MODIFIED="1397548184582"/>
<node TEXT="Shortcut: de folosit si ALT" ID="ID_1630221520" CREATED="1397548219370" MODIFIED="1397548225812"/>
<node TEXT="de ce avem nevoie de lower si upper? Florin?" ID="ID_1918416744" CREATED="1397548259980" MODIFIED="1397548269142"/>
<node TEXT="de fapt, de folosit dictionarul ca map, nu ca lista. De mapat dupa cheie. Avand in vedere ca o cheie are o lista" ID="ID_523744040" CREATED="1397548376500" MODIFIED="1397548409752"/>
<node TEXT="actionIdsToShortcuts:Dictionary" STYLE_REF="Code" ID="ID_71145200" CREATED="1397549688751" MODIFIED="1397549730663">
<node TEXT="la inregistrare shortcut, dc. targetul actions este string, (i.e. id actiune) =&gt; inregistram in ambele mapuri" ID="ID_235292997" CREATED="1397549731941" MODIFIED="1397549770514"/>
<node TEXT="folosit de rendererul de meniu" ID="ID_743070388" CREATED="1397549781242" MODIFIED="1397549816004">
<node TEXT="tooltip pentru actiunile de pe toolbar" ID="ID_1404826329" CREATED="1397550186763" MODIFIED="1397550202235"/>
</node>
</node>
</node>
<node TEXT="Editare shortcut din ecran" ID="ID_1601424826" CREATED="1397550008753" MODIFIED="1397550016595">
<node TEXT="eventual filtru, ca in eclipse" ID="ID_1013085494" CREATED="1397550112823" MODIFIED="1397550119305"/>
</node>
<node TEXT="Editare shrotcut ca in mm" ID="ID_1843952567" CREATED="1397550016993" MODIFIED="1397550022525">
<node TEXT="Un boolean, poate in KeyBindings" ID="ID_1245572319" CREATED="1397550433904" MODIFIED="1397550542796">
<node STYLE_REF="Code" ID="ID_132672593" CREATED="1397550504306" MODIFIED="1397550506106"><richcontent TYPE="NODE">

<html>
  <head>
    
  </head>
  <body>
    <p>
      learnShortcutOnNextActionInvocation:Boolean
    </p>
  </body>
</html>
</richcontent>
</node>
</node>
<node TEXT="s-ar emite un eveniment" ID="ID_1903796516" CREATED="1397550514334" MODIFIED="1397550520796"/>
<node TEXT="ne-ar intreba daca vrem in global sau per user" ID="ID_495456174" CREATED="1397550651914" MODIFIED="1397550661366"/>
</node>
<node TEXT="remember: shortcuturi si la iconite ca in mm" ID="ID_1409791559" CREATED="1397550168514" MODIFIED="1397550180215">
<node TEXT="o eticheta cu shrotcuturi; sau de afisat cu alta culoare butoanele care au shortcut" ID="ID_1730511873" CREATED="1397550248053" MODIFIED="1397550260185"/>
<node TEXT="am putea folosi ac. sistem, dar la aparitie popup =&gt; scos filtre; la close =&gt; repus filtre; cu ceva gen try/finally" ID="ID_1484245464" CREATED="1397550378833" MODIFIED="1397550400635"/>
</node>
<node TEXT="stocare" ID="ID_175589418" CREATED="1397550576894" MODIFIED="1397550578436">
<node TEXT="un nod cu preferinte generale" ID="ID_1922851389" CREATED="1397551118406" MODIFIED="1397551125128"/>
<node TEXT="un nod cu preferinte per user" ID="ID_1712298807" CREATED="1397551125397" MODIFIED="1397551131079"/>
<node TEXT="la login, le cerem, facem merge, si le procesam : introducere in bindings, respectiv introducere intr-o structura de data (desi pentru preferintele generale, i.e. doar proprietati, am putea pastra nodul)" ID="ID_482554113" CREATED="1397551131267" MODIFIED="1397551181499"/>
<node TEXT="pentru editare: am putea folosi oare sistemul de &quot;change&quot;? pentru ca tr. sa modificam optiunile master si user" ID="ID_93709617" CREATED="1397551258217" MODIFIED="1397551287239"/>
</node>
</node>
<node TEXT="Site" ID="ID_675208674" CREATED="1398777539314" MODIFIED="1398777540895">
<node TEXT="membrii" ID="ID_720604947" CREATED="1398777934835" MODIFIED="1398777939236">
<node TEXT="cc" STYLE_REF="Done (Blue)" ID="ID_272419828" CREATED="1398777940185" MODIFIED="1398777945586"/>
<node TEXT="ss" STYLE_REF="In progress 2 (Light Yellow)" ID="ID_1684782364" CREATED="1398777969136" MODIFIED="1398777981687"/>
<node TEXT="cs" STYLE_REF="In progress (Yellow)" ID="ID_1645211300" CREATED="1399277923669" MODIFIED="1399277925359"/>
</node>
<node TEXT="extensii" ID="ID_882637182" CREATED="1399277819998" MODIFIED="1399277824313">
<node TEXT="core" ID="ID_873406039" CREATED="1398777998706" MODIFIED="1398777999517">
<node TEXT="overview" STYLE_REF="In progress (Yellow)" ID="ID_1983099546" CREATED="1399277851934" MODIFIED="1399277877104"/>
<node TEXT="files" STYLE_REF="In progress 2 (Light Yellow)" ID="ID_1017551348" CREATED="1398778088246" MODIFIED="1399301932204">
<icon BUILTIN="button_ok"/>
</node>
<node TEXT="properties editor" STYLE_REF="In progress 2 (Light Yellow)" ID="ID_1589706454" CREATED="1398778005056" MODIFIED="1399301937339">
<icon BUILTIN="button_ok"/>
</node>
<node TEXT="upload, download" STYLE_REF="Done (Blue)" ID="ID_371806388" CREATED="1398778134296" MODIFIED="1399277123872">
<icon BUILTIN="button_ok"/>
</node>
<node TEXT="links" STYLE_REF="Done (Blue)" ID="ID_1655072323" CREATED="1398778154786" MODIFIED="1399371686855"/>
<node TEXT="worbench" ID="ID_1183494294" CREATED="1398778163736" MODIFIED="1398778167937"/>
<node TEXT="editor, dirty, ..." STYLE_REF="Done (Blue)" ID="ID_529199865" CREATED="1398778242016" MODIFIED="1399277510468"/>
</node>
<node TEXT="mobile" ID="ID_1844985996" CREATED="1398778371247" MODIFIED="1398778373288">
<node TEXT="overview" STYLE_REF="In progress (Yellow)" ID="ID_1684640948" CREATED="1399277851934" MODIFIED="1399277880544"/>
<node TEXT="accounts management" STYLE_REF="In progress 2 (Light Yellow)" ID="ID_1496702880" CREATED="1398778382997" MODIFIED="1399390003152">
<icon BUILTIN="button_ok"/>
</node>
<node TEXT="actions/menu" ID="ID_1046702103" CREATED="1398778415558" MODIFIED="1398778421559"/>
</node>
<node TEXT="mind-map" ID="ID_1768110366" CREATED="1398777882505" MODIFIED="1398777886446">
<node TEXT="overview" STYLE_REF="In progress (Yellow)" ID="ID_785046260" CREATED="1399277884677" MODIFIED="1399277897658">
<icon BUILTIN="button_ok"/>
</node>
<node TEXT="stiluri" STYLE_REF="Done (Blue)" ID="ID_851824789" CREATED="1398777887995" MODIFIED="1399277148727">
<icon BUILTIN="button_ok"/>
</node>
<node TEXT="stiluri sebi" STYLE_REF="In progress 2 (Light Yellow)" ID="ID_749378955" CREATED="1398777948665" MODIFIED="1399301943275">
<icon BUILTIN="button_ok"/>
</node>
<node TEXT="actiuni (inclusiv taste)" ID="ID_121319593" CREATED="1398778517518" MODIFIED="1399277487934">
<icon BUILTIN="help"/>
</node>
</node>
</node>
<node TEXT="hub" STYLE_REF="In progress (Yellow)" ID="ID_314433525" CREATED="1399277840422" MODIFIED="1399277920049"/>
<node TEXT="download" STYLE_REF="In progress (Yellow)" ID="ID_1467642938" CREATED="1399277904645" MODIFIED="1399277920041"/>
<node TEXT="articolul de blog" ID="ID_386030993" CREATED="1399277911124" MODIFIED="1399277917338"/>
<node TEXT="to update captures" ID="ID_516470351" CREATED="1399288789166" MODIFIED="1399288818044">
<node TEXT="/core/editor1.png" ID="ID_1539411422" CREATED="1399288818976" MODIFIED="1399288826734"/>
<node TEXT="/core/editor2.png" ID="ID_454960945" CREATED="1399288827137" MODIFIED="1399288834838"/>
<node TEXT="/core/links1.png" ID="ID_226567194" CREATED="1399371674936" MODIFIED="1399371681194"/>
<node TEXT="/mobile/*.png (all)" ID="ID_1944655252" CREATED="1399377848403" MODIFIED="1399389996762"/>
</node>
</node>
</node>
<node TEXT="core" STYLE_REF="In progress (Yellow)" FOLDED="true" POSITION="right" ID="ID_101957068" CREATED="1389867966943" MODIFIED="1390907627364">
<node TEXT="GH7 - Move existing MindMap Editor on the new project structure" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1899394697" CREATED="1389867972039" MODIFIED="1393926764774" LINK="https://github.com/flower-platform/flower-platform-4/issues/7">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_1546790363" CREATED="1390486208316" MODIFIED="1390486208326">
<node TEXT="RM8868 - GH7 - Move existing MindMap Editor on the new project structure" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_250204153" CREATED="1390486208326" MODIFIED="1390486208366" LINK="http://csp1/redmine/issues/8868"/>
<node TEXT="RM8869 - [Review] GH7 - Move existing MindMap Editor on the new project structure" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_156435855" CREATED="1390486208586" MODIFIED="1390486208596" LINK="http://csp1/redmine/issues/8869"/>
</node>
<node TEXT="Sincronizare&#xa;23/01/2014" STYLE_REF="Validated (Green)" ID="ID_966366100" CREATED="1390476028934" MODIFIED="1390477740856">
<icon BUILTIN="group"/>
<node TEXT="start" ID="ID_192850255" CREATED="1390477736845" MODIFIED="1390477737836"/>
<node TEXT="Setup new env" ID="ID_987468890" CREATED="1390476033053" MODIFIED="1390476037154">
<node TEXT="FB 4.7: default location" ID="ID_1149955578" CREATED="1390476038783" MODIFIED="1390476049384"/>
<node TEXT="d:/java/eclipse_4.3.1_modeling_x64" ID="ID_1743155054" CREATED="1390476065103" MODIFIED="1390476085394"/>
<node TEXT="copy wtp" ID="ID_1122286413" CREATED="1390476217892" MODIFIED="1390476230753"/>
<node TEXT="cp. link -&gt; dropins" ID="ID_1192568533" CREATED="1390476245021" MODIFIED="1390476255783"/>
<node TEXT="cp fb" ID="ID_796397452" CREATED="1390476260232" MODIFIED="1390476271303"/>
<node TEXT="rulare utilitarul de asociere: il asociezi cu ecl 3.7" ID="ID_311399438" CREATED="1390476271732" MODIFIED="1390476287333"/>
<node TEXT="comparare fb cu" ID="ID_1617911319" CREATED="1390476298692" MODIFIED="1390476306093">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_796397452" STARTINCLINATION="80;0;" ENDINCLINATION="80;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
</node>
</node>
<node TEXT="Expected results" ID="ID_711200861" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="prototype on new structure" ID="ID_943141039" CREATED="1390477745255" MODIFIED="1390477750956"/>
</node>
</node>
<node TEXT="GH103 - Parser-friendly fullNodeId" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_353521578" CREATED="1393938854851" MODIFIED="1395140788626" LINK="https://github.com/flower-platform/flower-platform-4/issues/103">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_167758484" CREATED="1394192479424" MODIFIED="1394192479454">
<node TEXT="RM9082 - GH103 - Parser-friendly fullNodeId" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_31126538" CREATED="1394192479454" MODIFIED="1394192479484" LINK="http://csp1/redmine/issues/9082"/>
<node TEXT="RM9083 - [Review] GH103 - Parser-friendly fullNodeId" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_196131937" CREATED="1394192479804" MODIFIED="1394192479814" LINK="http://csp1/redmine/issues/9083"/>
</node>
<node TEXT="eroarea de parsare pt full node id apare cand dai click direct pe diagrama; pare sa returneze ca selectat un nod fara tip, resursa si id si de acolo vine full node id = ||" ID="ID_1624190512" CREATED="1393938917956" MODIFIED="1393938945582"/>
<node TEXT="Observ inca o chestie: in flex si java, getFullNodeId sunt un pic diferite. As fi zis sa facem si in Java ca in Flex. Dar acum, daca schimbam , va fi altfel oricum. Deci fie adaugam acel Util.defaultIfNull in Java, fie o stergem din Flex (daca schimbam conventia de delimitare)" ID="ID_588986671" CREATED="1393944395873" MODIFIED="1393944487674">
<node TEXT="a fost creata metoda pe java, in GH76" ID="ID_907802380" CREATED="1394194914520" MODIFIED="1394195030539"/>
</node>
</node>
<node TEXT="GH29 - Unit tests for the Node API" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_104783127" CREATED="1391510691129" MODIFIED="1391510705040" LINK="https://github.com/flower-platform/flower-platform-4/issues/29">
<node TEXT="Redmine" ID="ID_1236355918" CREATED="1391510920120" MODIFIED="1391510920120">
<node TEXT="RM8932 - GH29 - Unit tests for the Node API" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1529840629" CREATED="1391510920120" MODIFIED="1391510920120" LINK="http://csp1/redmine/issues/8932"/>
<node TEXT="RM8933 - [Review] GH29 - Unit tests for the Node API" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1285029844" CREATED="1391510923790" MODIFIED="1391510923790" LINK="http://csp1/redmine/issues/8933"/>
</node>
<node TEXT="Expected results" ID="ID_44542126" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="Phase 1" ID="ID_692095704" CREATED="1391510724059" MODIFIED="1391510728090">
<node TEXT="add mock lib" ID="ID_213831277" CREATED="1391510729308" MODIFIED="1391511782038">
<node TEXT="https://code.google.com/p/mockito/" ID="ID_313216913" CREATED="1391511992388" MODIFIED="1391511992388" LINK="https://code.google.com/p/mockito/"/>
</node>
<node TEXT="make tree design" ID="ID_671660743" CREATED="1391511782257" MODIFIED="1391511796498"/>
<node TEXT="add some mocked children providers" ID="ID_1735540819" CREATED="1391511912577" MODIFIED="1391511921698"/>
<node TEXT="register them" ID="ID_1797933274" CREATED="1391511923256" MODIFIED="1391511928588"/>
<node TEXT="run test" ID="ID_1026761389" CREATED="1391511928826" MODIFIED="1391511930388"/>
</node>
<node TEXT="testGetChildren" ID="ID_215547138" CREATED="1391519704607" MODIFIED="1391519718028">
<node TEXT="test ordonare prioritati" ID="ID_492764971" CREATED="1391519881118" MODIFIED="1391519887659">
<node TEXT="pt A, ofera copii, C(order: 100), B (order: 0), A (order: -100)" ID="ID_1721833875" CREATED="1391519734517" MODIFIED="1391519839219"/>
</node>
<node TEXT="test categorii" ID="ID_472228133" CREATED="1391519892978" MODIFIED="1391519898509"/>
</node>
</node>
<node TEXT="Construction site" ID="ID_585750341" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="ChildrenProvider providerForTypeA = mock(CP.class)&#xa;when(mCP.get(new Node(&quot;a&quot;, null, 1))).thenReturn(&#xa;Arrays.asCollection(new Node[] { new Node(&quot;a&quot;, 12), (a, 3)} )" ID="ID_1822964437" CREATED="1391520391828" MODIFIED="1391520585840"/>
<node TEXT="assertEqauls(service.getChildren(&quot;a, 2&quot;), new List...)" ID="ID_809577200" CREATED="1391520722938" MODIFIED="1391520799140"/>
</node>
</node>
<node TEXT="GH49 - Save audit info into DB (primarily service, method, some params)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1905077450" CREATED="1392121749395" MODIFIED="1392121776077" LINK="https://github.com/flower-platform/flower-platform-4/issues/49"/>
<node TEXT="Build Script" STYLE_REF="In progress (Yellow)" ID="ID_281940464" CREATED="1398692879478" MODIFIED="1398694475993">
<node TEXT="GH254 - Build script" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_374051562" CREATED="1397657082038" MODIFIED="1398694433500" LINK="https://github.com/flower-platform/flower-platform-4/issues/254">
<node TEXT="Redmine" ID="ID_1125508383" CREATED="1398694459704" MODIFIED="1398694459705">
<node TEXT="RM9363 - GH254 - Build script" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1829410769" CREATED="1398694459706" MODIFIED="1398694459709" LINK="http://csp1/redmine/issues/9363"/>
<node TEXT="RM9364 - [Review] GH254 - Build script" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1643425371" CREATED="1398694460275" MODIFIED="1398694460280" LINK="http://csp1/redmine/issues/9364"/>
</node>
<node TEXT="Doc" FOLDED="true" ID="ID_1731534048" CREATED="1398693325578" MODIFIED="1398693327156">
<node TEXT="https://www.eclipse.org/tycho/" ID="ID_337286430" CREATED="1398692897212" MODIFIED="1398692897212" LINK="https://www.eclipse.org/tycho/"/>
<node TEXT="http://www.vogella.com/tutorials/EclipseTycho/article.html" ID="ID_1384382072" CREATED="1398693022700" MODIFIED="1398693022700" LINK="http://www.vogella.com/tutorials/EclipseTycho/article.html"/>
<node TEXT="http://blog.vogella.com/2013/01/03/tycho-advanced/" ID="ID_201627870" CREATED="1398693032411" MODIFIED="1398693032411" LINK="http://blog.vogella.com/2013/01/03/tycho-advanced/"/>
<node TEXT="/com.crispico.flower.flexdiagram.gantt/ant/build.xml" ID="ID_842049886" CREATED="1398693303934" MODIFIED="1398693306157"/>
<node TEXT="cu flex: cred flexmojos" ID="ID_993735460" CREATED="1398693998408" MODIFIED="1398694004125"/>
</node>
<node TEXT="de rulat teste" ID="ID_1090973858" CREATED="1398694262831" MODIFIED="1398694265526"/>
<node TEXT="Warning" FOLDED="true" ID="ID_610251520" CREATED="1398693330985" MODIFIED="1398693335110">
<node TEXT="de scris constantele de versiune: app + API, java &amp; flex (inc)" ID="ID_866383350" CREATED="1397657086158" MODIFIED="1397657101863"/>
<node TEXT="de pregatit un fisier de properties prin merge (de folosit scriptul merge-config-files.xml)" ID="ID_691872608" CREATED="1397657102712" MODIFIED="1397657473672"/>
<node TEXT="idem pentru log" ID="ID_916803396" CREATED="1397657473940" MODIFIED="1397657478133"/>
<node TEXT="din astea 2 vor rezulta fisierele &quot;master&quot;. Care la randul lor, vor putea fi override pe prod" ID="ID_491289994" CREATED="1397657478341" MODIFIED="1397657498033"/>
<node TEXT="dev" ID="ID_1473999033" CREATED="1398693546411" MODIFIED="1398693550308">
<node TEXT="flower-platform.properties" STYLE_REF="Planned (Orange)" ID="ID_1457195151" CREATED="1398693551333" MODIFIED="1398693791238">
<hook NAME="FirstGroupNode"/>
</node>
<node TEXT="logback.config" STYLE_REF="Planned (Orange)" ID="ID_480164697" CREATED="1398693563974" MODIFIED="1398693677211"/>
<node TEXT="comise in proj core" ID="ID_539377059" CREATED="1398693791235" MODIFIED="1398693805325">
<hook NAME="SummaryNode"/>
</node>
<node TEXT="flower-platform.properties.local" STYLE_REF="In progress 2 (Light Yellow)" ID="ID_1125306206" CREATED="1398693551333" MODIFIED="1398693809261">
<hook NAME="FirstGroupNode"/>
</node>
<node TEXT="logback.config.local" STYLE_REF="In progress 2 (Light Yellow)" ID="ID_1576465399" CREATED="1398693563974" MODIFIED="1398693594656"/>
<node TEXT="nu se comit" ID="ID_328970341" CREATED="1398693809258" MODIFIED="1398693811677">
<hook NAME="SummaryNode"/>
</node>
</node>
<node TEXT="build" ID="ID_1245247478" CREATED="1398693625779" MODIFIED="1398693630052">
<node TEXT="flower-platform.properties" STYLE_REF="Planned (Orange)" ID="ID_1107229572" CREATED="1398693551333" MODIFIED="1398693679400"/>
<node TEXT="logback.config" STYLE_REF="Planned (Orange)" ID="ID_457010103" CREATED="1398693563974" MODIFIED="1398693679409"/>
<node TEXT="flower-platform.properties.merge" STYLE_REF="Done (Blue)" ID="ID_784751431" CREATED="1398693551333" MODIFIED="1398693779629">
<hook NAME="FirstGroupNode"/>
</node>
<node TEXT="logback.config.merge" STYLE_REF="Done (Blue)" ID="ID_1263876972" CREATED="1398693563974" MODIFIED="1398693656904"/>
<node TEXT="comise in dir de build" ID="ID_1375166296" CREATED="1398693779622" MODIFIED="1398693787030">
<hook NAME="SummaryNode"/>
</node>
<node TEXT="rezulta:" ID="ID_52784096" CREATED="1398693659354" MODIFIED="1398693663088"/>
<node TEXT="flower-platform.properties" STYLE_REF="Validated (Green)" ID="ID_20971228" CREATED="1398693551333" MODIFIED="1398693816549">
<hook NAME="FirstGroupNode"/>
</node>
<node TEXT="logback.config" STYLE_REF="Validated (Green)" ID="ID_1330932718" CREATED="1398693563974" MODIFIED="1398693682034"/>
<node TEXT="incluse in distr" ID="ID_1716229024" CREATED="1398693816546" MODIFIED="1398693820655">
<hook NAME="SummaryNode"/>
</node>
</node>
<node TEXT="productie" ID="ID_817586609" CREATED="1398693825336" MODIFIED="1398693828761">
<node TEXT="flower-platform.properties" STYLE_REF="Validated (Green)" ID="ID_1132735828" CREATED="1398693551333" MODIFIED="1398693816549">
<hook NAME="FirstGroupNode"/>
</node>
<node TEXT="logback.config" STYLE_REF="Validated (Green)" ID="ID_1797929624" CREATED="1398693563974" MODIFIED="1398693682034"/>
<node TEXT="flower-platform.properties.local" STYLE_REF="Resubmitted (Violet)" ID="ID_1909728043" CREATED="1398693551333" MODIFIED="1398693854901">
<hook NAME="FirstGroupNode"/>
</node>
<node TEXT="logback.config.local" STYLE_REF="Resubmitted (Violet)" ID="ID_152762261" CREATED="1398693563974" MODIFIED="1398693854908"/>
</node>
</node>
<node TEXT="Sincronizare&#xa;29/04/2014" ID="ID_774981699" CREATED="1398770718591" MODIFIED="1398770720645">
<icon BUILTIN="group"/>
<node TEXT="Cum putem lua pluginurile de pe hard. Adica sa nu le mai descarce el.&#xa;Atat pentru compilare, cat si pentru build de produs." ID="ID_1990699370" CREATED="1398770729667" MODIFIED="1398770778029"/>
<node TEXT="Cum putem face build de produs?" ID="ID_1810249401" CREATED="1398770779809" MODIFIED="1398770789459">
<node TEXT="vogella vroia sa faca tutorial, dar cred ca nu a mai facut\" ID="ID_803645893" CREATED="1398770790179" MODIFIED="1398770804835"/>
<node TEXT="dar cred ca se poate: sa ne genereze un eclipse, cu pluginurile noastre + cele dependente" ID="ID_1400235167" CREATED="1398770805053" MODIFIED="1398770825669"/>
</node>
<node TEXT="Putem specifica ca anumite pluginuri sa le despacheteze? Altfel cel mai simplu ar fi sa dezarhivam" ID="ID_235558341" CREATED="1398770827537" MODIFIED="1398770866579"/>
<node TEXT="sa nu uiti de public-resoruces" ID="ID_1557672460" CREATED="1398770998533" MODIFIED="1398771201721"/>
<node TEXT="versiune plugin: sincronizata intre numele bundle si versiunea" ID="ID_767232983" CREATED="1398771293755" MODIFIED="1398771309121"/>
</node>
</node>
</node>
<node TEXT="Node Service and Nodes" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_476425693" CREATED="1392220482197" MODIFIED="1393606635223">
<node TEXT="Lazy Population of a Node" STYLE_REF="Validated (Green)" ID="ID_940535557" CREATED="1392220494446" MODIFIED="1393606631906">
<node TEXT="GH54 - Lazy Population of a Node" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1270445958" CREATED="1392220494446" MODIFIED="1393606627604" LINK="https://github.com/flower-platform/flower-platform-4/issues/54">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_1136232683" CREATED="1392299950870" MODIFIED="1392299950898">
<node TEXT="RM9002 - GH54 - Lazy Population of a Node" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_176522553" CREATED="1392299950901" MODIFIED="1392299950926" LINK="http://csp1/redmine/issues/9002"/>
<node TEXT="RM9003 - [Review] GH54 - Lazy Population of a Node" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1636656827" CREATED="1392299951577" MODIFIED="1392299951587" LINK="http://csp1/redmine/issues/9003"/>
</node>
<node TEXT="Expected results" ID="ID_481662738" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
</node>
<node TEXT="Construction site" ID="ID_768925455" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="Node.populated" STYLE_REF="Code" ID="ID_549487129" CREATED="1392220542710" MODIFIED="1392282473115"/>
<node TEXT="Node.getOrPopulateProperties()" STYLE_REF="Code" ID="ID_1080393037" CREATED="1392282484094" MODIFIED="1392282492765">
<node TEXT="if ! populated =&gt; populates" ID="ID_257739166" CREATED="1392220555822" MODIFIED="1392220565541"/>
</node>
<node TEXT="Node.rawNodeData" STYLE_REF="Code" ID="ID_1192828690" CREATED="1392282503243" MODIFIED="1392282594775">
<node TEXT="transient" ID="ID_1961819515" CREATED="1392282511354" MODIFIED="1392282513115"/>
<node TEXT="setabil doar prin constructor (ch prov), sau getOrRetrieve" ID="ID_1811958710" CREATED="1392282688463" MODIFIED="1392282763485"/>
</node>
<node TEXT="Node.rawNodeDataRetrieved" STYLE_REF="Code" ID="ID_1746070380" CREATED="1392282552833" MODIFIED="1392282747405"/>
<node TEXT="Node.getOrRetrieveRawNodeData()" STYLE_REF="Code" ID="ID_511033197" CREATED="1392282515443" MODIFIED="1392282754365">
<node TEXT="if ! init =&gt; init" ID="ID_487574891" CREATED="1392282549123" MODIFIED="1392282588875"/>
<node TEXT="atentie; poate sa nu existe RawNDProvider (cazul DB)" ID="ID_1101445313" CREATED="1392282925395" MODIFIED="1392282939026"/>
</node>
<node TEXT="Node(type, res, id, rawNodeData)" STYLE_REF="Code" ID="ID_1901622042" CREATED="1392282674893" MODIFIED="1392282767485"/>
<node TEXT="mm chProv: foloseste getOrRetrieve" ID="ID_722339930" CREATED="1392282842374" MODIFIED="1392282854426"/>
</node>
</node>
</node>
</node>
<node TEXT="Controllers and Descriptors" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_1929950192" CREATED="1392111158464" MODIFIED="1394709978828">
<node TEXT="Add Child Descriptor" STYLE_REF="Validated (Green)" ID="ID_1075541970" CREATED="1392111182583" MODIFIED="1394709979988">
<node TEXT="GH45 - AddChildDescriptor" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_417057896" CREATED="1392111182583" MODIFIED="1394709983398" LINK="https://github.com/flower-platform/flower-platform-4/issues/45">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_833965557" CREATED="1392136083646" MODIFIED="1392136083648">
<node TEXT="RM8984 - GH45 - AddChildDescriptor" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_255511632" CREATED="1392136083649" MODIFIED="1392136083671" LINK="http://csp1/redmine/issues/8984"/>
<node TEXT="RM8985 - [Review] GH45 - AddChildDescriptor" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_200955690" CREATED="1392136089966" MODIFIED="1392136089969" LINK="http://csp1/redmine/issues/8985"/>
</node>
</node>
</node>
</node>
<node TEXT="Resource Node and Updates" STYLE_REF="In progress (Yellow)" FOLDED="true" ID="ID_1374978353" CREATED="1392907056205" MODIFIED="1394026218500"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      A resource node is a node that can be a root in an editor. Several things are related to it: saving updates, resource management (for file system persistence), lock, etc
    </p>
  </body>
</html>
</richcontent>
<node TEXT="GH76 - RootNodeInfo: first iteration, with resource management" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_463903544" CREATED="1392907148235" MODIFIED="1392907597824" LINK="https://github.com/flower-platform/flower-platform-4/issues/76">
<node TEXT="Redmine" FOLDED="true" ID="ID_762462729" CREATED="1392907601784" MODIFIED="1392907601794">
<node TEXT="RM9043 - GH76 - RootNodeInfo: first iteration, with resource management" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1001872705" CREATED="1392907601794" MODIFIED="1392907601804" LINK="http://csp1/redmine/issues/9043"/>
<node TEXT="RM9044 - [Review] GH76 - RootNodeInfo: first iteration, with resource management" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_637834017" CREATED="1392907602324" MODIFIED="1392907602334" LINK="http://csp1/redmine/issues/9044"/>
</node>
<node TEXT="Construction site" ID="ID_1582281178" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
</node>
<node TEXT="Sincronizare&#xa;20/02/2014" STYLE_REF="Validated (Green)" ID="ID_1728479412" CREATED="1392910918146" MODIFIED="1392910926866">
<icon BUILTIN="group"/>
<node TEXT="discutia initiala" ID="ID_1518136148" CREATED="1392910922213" MODIFIED="1392910925032"/>
<node TEXT="RootNodeInfoDAO" STYLE_REF="Code" ID="ID_1550192243" CREATED="1392284983681" MODIFIED="1392285092833">
<node TEXT="addUpdate(rootNode, update)" ID="ID_778001599" CREATED="1392285069801" MODIFIED="1392285077483"/>
<node TEXT="getUpdates(rootNode, timestamp)" ID="ID_395004650" CREATED="1392285077771" MODIFIED="1392908740789">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_1150867479" STARTINCLINATION="244;0;" ENDINCLINATION="244;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
<node TEXT="subscribe(sesId, rootNode)" STYLE_REF="Code" ID="ID_859685369" CREATED="1392285205981" MODIFIED="1392908515481">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_545564231" STARTINCLINATION="263;0;" ENDINCLINATION="263;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
<node TEXT="apel prin nodeService.subscribe(): clientul cand deschide un editor" ID="ID_142530254" CREATED="1392908122352" MODIFIED="1392908159066"/>
<node TEXT="actualizare map sesiuni" ID="ID_241117578" CREATED="1392908613984" MODIFIED="1392908617960"/>
</node>
<node TEXT="unsubscribe(sesId, rootNode)" STYLE_REF="Code" ID="ID_1263447761" CREATED="1392285217342" MODIFIED="1392908510529">
<node TEXT="apel prin inchiderea editorului" ID="ID_1798371443" CREATED="1392908529374" MODIFIED="1392908537203"/>
<node TEXT="apel la &quot;expirare sesiune&quot;" ID="ID_1250162693" CREATED="1392908537402" MODIFIED="1392908542774"/>
</node>
<node TEXT="stillSubscribedPing(sesId, rootNode)" STYLE_REF="Code" ID="ID_1150867479" CREATED="1392908665680" MODIFIED="1392908732440">
<node TEXT="expus si de serviciu" ID="ID_73160037" CREATED="1392908754227" MODIFIED="1392908759453"/>
</node>
<node TEXT="sessionCreated(sesId)" STYLE_REF="Code" ID="ID_1828242572" CREATED="1392285316881" MODIFIED="1392908954472">
<node TEXT="apelat de listener" ID="ID_382859995" CREATED="1392908955147" MODIFIED="1392908958746"/>
</node>
<node TEXT="updateSessionProperty(sesId, prop, value)" STYLE_REF="Code" ID="ID_59302359" CREATED="1392285482542" MODIFIED="1392909064415">
<node TEXT="ip" ID="ID_1317641295" CREATED="1392285499232" MODIFIED="1392285501224"/>
<node TEXT="username" ID="ID_480073209" CREATED="1392285501452" MODIFIED="1392285502894"/>
<node TEXT="apelat la creere sesiune; sigur cu ip; poate si user" ID="ID_594602685" CREATED="1392909023208" MODIFIED="1392909039204"/>
<node TEXT="apelat la login, logout (cu user)" ID="ID_1974623496" CREATED="1392909039426" MODIFIED="1392909046022"/>
</node>
<node TEXT="sessionRemoved" STYLE_REF="Code" ID="ID_1255563264" CREATED="1392285505972" MODIFIED="1392909066355">
<node TEXT="apelat de listener (dar nu e 100% fiabil)" ID="ID_1936898185" CREATED="1392909067863" MODIFIED="1392909077975"/>
</node>
</node>
<node TEXT="SessionUnsubscriber" STYLE_REF="Code" ID="ID_346094828" CREATED="1392908860384" MODIFIED="1392908869016">
<node TEXT="bazat pe RNID (nu InMemory...), vede care nu mai sunt, si face unsubcribe" ID="ID_611440991" CREATED="1392908871310" MODIFIED="1392908896399"/>
</node>
<node TEXT="RootNodeSubscribtionListener" STYLE_REF="Code" ID="ID_545564231" CREATED="1392287116524" MODIFIED="1392287131112">
<node TEXT="teoretic implem doar de InMem" ID="ID_1829291124" CREATED="1392908310163" MODIFIED="1392908319395"/>
<node TEXT="deci cunoaste si face cast catre clasele InMemory" ID="ID_778955902" CREATED="1392908377158" MODIFIED="1392908386868"/>
<node TEXT="firstClientSubscribed()" STYLE_REF="Code" ID="ID_115076043" CREATED="1392287133062" MODIFIED="1392908479396">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_1339058643" STARTINCLINATION="246;0;" ENDINCLINATION="246;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
<node TEXT="lastClientUnsubscribed()" STYLE_REF="Code" ID="ID_1531405526" CREATED="1392287144958" MODIFIED="1392908486182">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_1339058643" STARTINCLINATION="236;0;" ENDINCLINATION="236;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
</node>
<node TEXT="InMemoryRNID" STYLE_REF="Code" ID="ID_494329655" CREATED="1392285093811" MODIFIED="1392285112863">
<node TEXT="map&lt;Node, InMemoryRNI&gt;" ID="ID_1376469763" CREATED="1392287256232" MODIFIED="1392287264546"/>
<node TEXT="map&lt;sess, rootNode&gt;" ID="ID_664373967" CREATED="1392287349291" MODIFIED="1392287360810"/>
<node TEXT="map&lt;sess, props[ip, user, etc]&gt;" ID="ID_514692718" CREATED="1392287426153" MODIFIED="1392287440870"/>
</node>
<node TEXT="InMemoryRootNodeInfo" STYLE_REF="Code" ID="ID_1039345900" CREATED="1392285114912" MODIFIED="1392285142193">
<node TEXT="updates" ID="ID_300988288" CREATED="1392287289279" MODIFIED="1392287293670"/>
<node TEXT="map&lt;resourceId, resource&gt;" ID="ID_1339058643" CREATED="1392287296708" MODIFIED="1392287307592"/>
<node TEXT="sessions" ID="ID_192248193" CREATED="1392287409567" MODIFIED="1392287412250"/>
</node>
<node TEXT="reactivat link handler, pentru deschidere editor." ID="ID_700628955" CREATED="1392909363883" MODIFIED="1392909375602">
<node TEXT="URL" ID="ID_1271847826" CREATED="1392909376617" MODIFIED="1392909378259"/>
<node TEXT="JS" ID="ID_1176288000" CREATED="1392909378450" MODIFIED="1392909380626"/>
<node TEXT="UI" ID="ID_1216697456" CREATED="1392909380808" MODIFIED="1392909381646"/>
</node>
<node TEXT="trebuie sa facem pe client acea lista de NodeUpdateProcessor. De fapt map fullNodeId -&gt; DiaShell (contine un NodeUpdateProcessor, descriptor)" ID="ID_1360109713" CREATED="1392653922982" MODIFIED="1392909480194"/>
<node TEXT="Hardcodam la root vreo 3-4 noduri" ID="ID_1858802726" CREATED="1392909595014" MODIFIED="1392909604849"/>
<node TEXT="info: RawNodeDataProvider, furnizat de mm, e responsabil sa rezolve nodul la un nod real" ID="ID_1455565" CREATED="1392910355177" MODIFIED="1392910384230"/>
<node TEXT="idee: pentru debug, putem face niste ch provideri, ca sa vedem tree-ul" ID="ID_102958075" CREATED="1391782916520" MODIFIED="1393948242733">
<font BOLD="true"/>
</node>
<node TEXT="tree de debug" ID="ID_1763336367" CREATED="1392910813724" MODIFIED="1393948240865">
<font BOLD="true"/>
</node>
</node>
<node TEXT="Sincronizare&#xa;05/03/2014" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_522937879" CREATED="1394023725413" MODIFIED="1394035903534">
<icon BUILTIN="group"/>
<node TEXT="ResourceInfoService" STYLE_REF="Code" ID="ID_641247" CREATED="1394034572507" MODIFIED="1394034821731">
<node TEXT="Ar merge poate pus refresh: in NodeService" ID="ID_1863149017" CREATED="1393949312656" MODIFIED="1393949325347"/>
</node>
<node TEXT="ResourceInfoServiceRemote" STYLE_REF="Code" ID="ID_139517985" CREATED="1394026826651" MODIFIED="1394028478889">
<node TEXT="ping()" ID="ID_1128808450" CREATED="1394028478889" MODIFIED="1394028478899"/>
<node TEXT="subscribeToParentResource(id):Node" ID="ID_892795139" CREATED="1394028483487" MODIFIED="1394028676070"/>
</node>
<node TEXT="ResourceInfoDAO" STYLE_REF="Code" ID="ID_1910666527" CREATED="1394023733561" MODIFIED="1394024770746">
<node TEXT="sessionSubscribedToResource(id, session)" ID="ID_1794955790" CREATED="1394023760561" MODIFIED="1394024250153"/>
<node TEXT="sessionUnsubscribedFromResource(id, session)" ID="ID_159830080" CREATED="1394023805202" MODIFIED="1394024263084"/>
<node TEXT="sessionCreated(session)" ID="ID_1700818014" CREATED="1394024285693" MODIFIED="1394024316075"/>
<node TEXT="sessionRemoved(session)" ID="ID_1871338325" CREATED="1394024325023" MODIFIED="1394024333205"/>
<node TEXT="getSessionProperty()" ID="ID_1346292758" CREATED="1394023973131" MODIFIED="1394023979223"/>
<node TEXT="updateSessionProperty()" ID="ID_857060498" CREATED="1394023973131" MODIFIED="1394023985733"/>
<node TEXT="getSessionsSubscribedToResource(id)" ID="ID_837389433" CREATED="1394023810091" MODIFIED="1394024472665"/>
<node TEXT="getResourcesSubscribedBySession(session)" ID="ID_113628453" CREATED="1394024350733" MODIFIED="1394024502995"/>
</node>
<node TEXT="InMemoryResourceInfoDAO" STYLE_REF="Code" ID="ID_1999912339" CREATED="1394024547205" MODIFIED="1394024770756">
<node TEXT="Map&lt;node id, RootNodeInfo&gt; resourceNodeIdToInfo" ID="ID_837573319" CREATED="1394024557724" MODIFIED="1394024630246"/>
<node TEXT="Map&lt;session id, SessionInfo&gt; sessionIdToSessionInfo" ID="ID_1423300860" CREATED="1394024614574" MODIFIED="1394024649616"/>
</node>
<node TEXT="SessionInfo" STYLE_REF="Code" ID="ID_1763929966" CREATED="1394024652794" MODIFIED="1394024770766">
<node TEXT="subscribedResourceNodeIds" ID="ID_1912085802" CREATED="1394024655915" MODIFIED="1394024687976"/>
<node TEXT="properties" ID="ID_604667093" CREATED="1394024688414" MODIFIED="1394024699726"/>
</node>
<node TEXT="RootNodeInfo" STYLE_REF="Code" ID="ID_533096913" CREATED="1394024719904" MODIFIED="1394024770766">
<node TEXT="rawResourceData" ID="ID_365941244" CREATED="1394027232023" MODIFIED="1394027238455"/>
<node TEXT="sessions" ID="ID_1249156109" CREATED="1394024724504" MODIFIED="1394024732436"/>
<node TEXT="updates" ID="ID_958591791" CREATED="1394024732724" MODIFIED="1394024734546"/>
<node TEXT="updateRequestedTimestamp" ID="ID_1294419240" CREATED="1394024752614" MODIFIED="1394025690500"/>
</node>
<node TEXT="ResourceSubscriptionListener" STYLE_REF="Code" ID="ID_859374307" CREATED="1394026028308" MODIFIED="1394026090290">
<node TEXT="FreeplaneResourceSubscriptionListener" STYLE_REF="Code" ID="ID_1052458732" CREATED="1394026028308" MODIFIED="1394026090300">
<node TEXT="does load/unload" ID="ID_1716126515" CREATED="1394026071988" MODIFIED="1394026077400"/>
</node>
</node>
<node TEXT="o resursa da: SubscriptionListener + ChildrenProvider. Face test de extensie sa stie daca se executa. Pt. freeplane, e acelasi ChildrenProv si pentru noduri. Cand furnizeaza copii, pune calea sub forma tipResursa://cale. Dar eu subscriu pe fullNodeId al fisierului" ID="ID_1860668974" CREATED="1394032834742" MODIFIED="1394033013924"/>
<node TEXT="poate esua incarcarea in caz de nod eronat" ID="ID_541809818" CREATED="1394034891419" MODIFIED="1394034899731"/>
</node>
<node TEXT="Expected results#" ID="ID_951408140" CREATED="1389360413465" MODIFIED="1394027587316">
<icon BUILTIN="revision"/>
<node TEXT="Request updates" ID="ID_716923738" CREATED="1394027599396" MODIFIED="1394027599426" LINK="#ID_345419718"/>
<node TEXT="Updates to self at the end of remote invocation" ID="ID_102227452" CREATED="1394027599426" MODIFIED="1394027599436" LINK="#ID_706078003"/>
<node TEXT="Editor Frontend Base" ID="ID_1408702354" CREATED="1394034228839" MODIFIED="1394034228849" LINK="#ID_1998124581"/>
<node TEXT="Cross refs: resource node id &lt;-&gt; editors" ID="ID_1261755385" CREATED="1394034228859" MODIFIED="1394034228859" LINK="#ID_178241386"/>
<node TEXT="Timer for requesting updates" ID="ID_1671079444" CREATED="1394034228859" MODIFIED="1394034228859" LINK="#ID_330818403"/>
<node TEXT="During remote method invocation, enrich server message with info (in headers)" ID="ID_1539197998" CREATED="1394034228859" MODIFIED="1394034228869" LINK="#ID_386968615"/>
<node TEXT="Debug node providers for session info" ID="ID_450134115" CREATED="1394034301969" MODIFIED="1394034301979" LINK="#ID_1790078353"/>
<node TEXT="Automatic unsubcription. Resources that have not been &quot;pinged&quot; for a certain periond(i.e. request updates for them) =&gt; perform unsubscribe, which might trigger the resource unload (if the last subscriber left)." ID="ID_1799831010" CREATED="1394034311129" MODIFIED="1394034311129" LINK="#ID_100261305"/>
<node TEXT="Suscribe/unsubscribe hooks, that would allow resources to load/unload." ID="ID_1113955868" CREATED="1394034311139" MODIFIED="1394034311149" LINK="#ID_42826433"/>
</node>
<node TEXT="Sincronizare&#xa;14/03/2014" STYLE_REF="Validated (Green)" ID="ID_1275238404" CREATED="1394791157490" MODIFIED="1394806713976">
<icon BUILTIN="group"/>
<node TEXT="campul resursa: tine nodul de resursa. =&gt; reformatat id-ul in format: (||) care sa suporte nesting. Campul asta e dat doar de childrenProvider. Sunt alte locuri acum in care se populeaza res (am vazut pe undeva prin flex: AddNodeAction; javaParameter?)?" ID="ID_1258944270" CREATED="1394791168158" MODIFIED="1394793592799"/>
<node TEXT="remove &quot;RESOURCE_KEY&quot;; on creation, client shouldn&apos;t fill resource or id" ID="ID_1760167848" CREATED="1394797092852" MODIFIED="1394797094436"/>
<node TEXT="subscribeToSelfOrParentResource" ID="ID_272610424" CREATED="1394726160897" MODIFIED="1394726172258"/>
<node TEXT="renuntam la RootNodeProvider. Mereu vom lua nodul root (resursa) direct din campul de resursa. Inclusiv cele care fac record." ID="ID_429673045" CREATED="1394791287739" MODIFIED="1394791833589"/>
<node TEXT="Nodurile de code, config; tip resursa: freeplanePersistence" ID="ID_166264299" CREATED="1394792283071" MODIFIED="1394792318118">
<node TEXT="deci renuntam la tigania curenta, in care convietuiesc cele 2 tipuri de resursa in ac. resursa" ID="ID_1872785219" CREATED="1394792411570" MODIFIED="1394792433018"/>
</node>
<node TEXT="PropertiesProvider: care zic pe un nod: isSubscribable" ID="ID_1498918974" CREATED="1394792449625" MODIFIED="1394792471308"/>
<node TEXT="SubscrListener: pune pe langa rawNodeData si resourceCategory" ID="ID_1801507138" CREATED="1394792484556" MODIFIED="1394792511906"/>
<node TEXT="NodeUpdateProcessor" ID="ID_114129002" CREATED="1394792129504" MODIFIED="1394792134319">
<node TEXT="comasare" ID="ID_1683695723" CREATED="1394792179971" MODIFIED="1394792182462"/>
<node TEXT="sa tina el nodurile resursa la care am facut subscribe" ID="ID_761149814" CREATED="1394792136214" MODIFIED="1394792151006"/>
<node TEXT="inchidere de noduri: daca era subscribable, sa il scoatem din lista" ID="ID_63846121" CREATED="1394791998675" MODIFIED="1394792011950"/>
<node TEXT="evident: actualizare si mapul global (resNod -&gt; editor/nodeUpdProc)" ID="ID_773936103" CREATED="1394792155291" MODIFIED="1394792173540"/>
<node TEXT="la expandare nod: daca un nod e subscribable =&gt; face si subscribe" ID="ID_1052078244" CREATED="1394792597290" MODIFIED="1394792627363"/>
</node>
<node TEXT="casuta de subscribe: sa ia un fullNodeId. Astfel vom putea deschide si pe root||" ID="ID_986801297" CREATED="1394726788564" MODIFIED="1394726817781"/>
<node TEXT="debug tree" ID="ID_621507198" CREATED="1394793312842" MODIFIED="1394793315123">
<node TEXT="debug tree in root" ID="ID_1053998952" CREATED="1394726818003" MODIFIED="1394726826711"/>
<node TEXT="sesiune" ID="ID_1042995557" CREATED="1394793320471" MODIFIED="1394793323684">
<node TEXT="file" ID="ID_712009782" CREATED="1394793324922" MODIFIED="1394793326691">
<node TEXT="sesiune2" ID="ID_705780565" CREATED="1394793327691" MODIFIED="1394793330331"/>
</node>
</node>
<node TEXT="file&apos;" ID="ID_1467886133" CREATED="1394793332023" MODIFIED="1394793369147">
<node TEXT="sesiune2&apos;" ID="ID_1408747853" CREATED="1394793346986" MODIFIED="1394793365527"/>
</node>
<node TEXT="stergere &quot;OPEN_ROOT&quot; din linkHandler deci. Si ce mai implica el" ID="ID_1755032691" CREATED="1394797205770" MODIFIED="1394797233155"/>
</node>
<node TEXT="clonare updates: responsabilitate procesor, si nu *ServiceLocator" ID="ID_509043458" CREATED="1394726756820" MODIFIED="1394726781153"/>
</node>
</node>
<node TEXT="GH158 - Refactor: rename rootNode -&gt; resourceNode;" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_259795581" CREATED="1395233298884" MODIFIED="1395233367157" LINK="https://github.com/flower-platform/flower-platform-4/issues/158">
<node TEXT="ResourceInfoService -&gt; ResourceService" ID="ID_1726837879" CREATED="1395233332668" MODIFIED="1395233347017"/>
<node TEXT="Redmine" ID="ID_1785401132" CREATED="1395746476046" MODIFIED="1395746476056">
<node TEXT="RM9202 - GH158 - Refactor: rename rootNode -&gt; resourceNode;" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1560753698" CREATED="1395746476056" MODIFIED="1395746476076" LINK="http://csp1/redmine/issues/9202"/>
<node TEXT="RM9203 - [Review] GH158 - Refactor: rename rootNode -&gt; resourceNode;" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_70017901" CREATED="1395746476406" MODIFIED="1395746476406" LINK="http://csp1/redmine/issues/9203"/>
</node>
</node>
<node TEXT="GH160 - Resource reload" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_522275057" CREATED="1395234359593" MODIFIED="1395234368179" LINK="https://github.com/flower-platform/flower-platform-4/issues/160">
<node TEXT="Construction site" ID="ID_1086970208" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="Reload action added to file menu, works with active editor frontend" ID="ID_796690984" CREATED="1395844429751" MODIFIED="1395844470115"/>
<node TEXT="on click" ID="ID_387117610" CREATED="1395844499937" MODIFIED="1395844502399">
<node TEXT="if editor has single resourceNodeId =&gt; request reload on server" ID="ID_852327542" CREATED="1395844530069" MODIFIED="1395844544281"/>
<node TEXT="if editor has multiple resourceNodeIds =&gt; show resources dialog to chose which to reload" ID="ID_509747199" CREATED="1395844440541" MODIFIED="1395844491985"/>
</node>
<node TEXT="server: reload re-sets the rawResourceData =&gt; updates are cleared, loadedTimestamp is updated" ID="ID_827569543" CREATED="1395844550689" MODIFIED="1395844601023"/>
<node TEXT="TODO: maybe send a NullUpdate(resourceNodeId) instead of simply null to force refresh; in this case, the client can invoke refresh only for the subtree starting from resourceNode, not for the entire mm" ID="ID_142995895" CREATED="1395844606501" MODIFIED="1395844665618"/>
</node>
<node TEXT="Redmine" ID="ID_302064255" CREATED="1395764230476" MODIFIED="1395764230486">
<node TEXT="RM9212 - GH160 - Resource reload" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_539072178" CREATED="1395764230488" MODIFIED="1395764230519" LINK="http://csp1/redmine/issues/9212"/>
<node TEXT="RM9213 - [Review] GH160 - Resource reload" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1986815873" CREATED="1395764230846" MODIFIED="1395764230851" LINK="http://csp1/redmine/issues/9213"/>
</node>
</node>
<node TEXT="Automatic unsubcription. Resources that have not been &quot;pinged&quot; for a certain periond(i.e. request updates for them) =&gt; perform unsubscribe, which might trigger the resource unload (if the last subscriber left)." ID="ID_100261305" CREATED="1394025695858" MODIFIED="1394034311139" LINK="#ID_1799831010"/>
<node TEXT="Suscribe/unsubscribe hooks, that would allow resources to load/unload." ID="ID_42826433" CREATED="1394025992718" MODIFIED="1394034311149" LINK="#ID_1113955868"/>
<node TEXT="Debug node providers for session info" ID="ID_1790078353" CREATED="1394034283667" MODIFIED="1394034301979" LINK="#ID_450134115"/>
<node TEXT="Node Updates" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_340077874" CREATED="1390905927213" MODIFIED="1393606396851"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      Controllers that record interactions with nodes, using timestamps. Clients pool this records, in order to update their UI
    </p>
  </body>
</html>
</richcontent>
<node TEXT="Controllers that record interactions" STYLE_REF="Validated (Green)" ID="ID_1648614363" CREATED="1390905981620" MODIFIED="1394026187970">
<node TEXT="GH24 - MindMap Editor: updates (w/ server side state)" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_466457816" CREATED="1389389774105" MODIFIED="1393606248987" LINK="https://github.com/flower-platform/flower-platform-4/issues/24">
<icon BUILTIN="flag-green"/>
<node TEXT="Expected results" FOLDED="true" ID="ID_332989049" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="Keep 2 implementations: w/o and w/ server state" ID="ID_1468813750" CREATED="1390907436901" MODIFIED="1390907456534"/>
</node>
<node TEXT="Construction site" FOLDED="true" ID="ID_255723998" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="first design" ID="ID_1772834169" CREATED="1391778891000" MODIFIED="1391778894401">
<node TEXT="Unit tests" ID="ID_813276688" CREATED="1390907708086" MODIFIED="1390907713297"/>
<node TEXT="Session listener adds/removes in SesionIdDAO" ID="ID_772650140" CREATED="1390905997455" MODIFIED="1390906078981"/>
<node TEXT="SessionIdDAO" STYLE_REF="Code" ID="ID_1309344869" CREATED="1390906016119" MODIFIED="1390906087967">
<node TEXT="addSession(sessionId)" STYLE_REF="Code" ID="ID_238619446" CREATED="1390906030743" MODIFIED="1390906087973"/>
<node TEXT="removeSession(sessionId)" STYLE_REF="Code" ID="ID_1813066200" CREATED="1390906041037" MODIFIED="1390906087978"/>
<node TEXT="sessionExists(sessionId)" STYLE_REF="Code" ID="ID_1191162794" CREATED="1390906045383" MODIFIED="1390906087981"/>
</node>
<node TEXT="NodeService" STYLE_REF="Code" ID="ID_1030283515" CREATED="1390906743797" MODIFIED="1390906872848">
<node TEXT="expand()" STYLE_REF="Code" ID="ID_1945695262" CREATED="1390906752360" MODIFIED="1390906872853"/>
<node TEXT="collapse()" STYLE_REF="Code" ID="ID_1281601677" CREATED="1390906761249" MODIFIED="1390906872854"/>
<node TEXT="Each method that modifies" ID="ID_549589339" CREATED="1390907066322" MODIFIED="1390907076337">
<node TEXT="Retrieves sessions" ID="ID_1219030634" CREATED="1390907077541" MODIFIED="1390907087349"/>
<node TEXT="For each session: add*Update" ID="ID_1731416855" CREATED="1390907087540" MODIFIED="1390907101672"/>
<node TEXT="If session not valid any more =&gt; delete session" ID="ID_1289413975" CREATED="1390907103462" MODIFIED="1390907120646"/>
</node>
<node TEXT="checkForUpdates()" STYLE_REF="Code" ID="ID_263503055" CREATED="1390906281567" MODIFIED="1390907134862">
<node TEXT="retrieves data" ID="ID_1472678221" CREATED="1390907179267" MODIFIED="1390907184349"/>
<node TEXT="doesn&apos;t use t/s" ID="ID_322209147" CREATED="1390907136865" MODIFIED="1390907179053"/>
<node TEXT="removes data" ID="ID_100673041" CREATED="1390907185911" MODIFIED="1390907188500"/>
</node>
</node>
<node TEXT="NodeExpandedStateDAO" STYLE_REF="Code" ID="ID_643899464" CREATED="1390906795465" MODIFIED="1390906914238">
<node TEXT="addSession(nodeId, sessionId)" STYLE_REF="Code" ID="ID_985301103" CREATED="1390906820928" MODIFIED="1390906914247"/>
<node TEXT="removeSession(nodeId, sessionId)" STYLE_REF="Code" ID="ID_630604023" CREATED="1390906861084" MODIFIED="1390906914249"/>
<node TEXT="getSessions(nodeId)" STYLE_REF="Code" ID="ID_394548705" CREATED="1390906884366" MODIFIED="1390906914251"/>
</node>
<node TEXT="NodeUpdateDAO" STYLE_REF="Code" ID="ID_39274231" CREATED="1390906235126" MODIFIED="1390906252313">
<node TEXT="all methods have a sessionId" ID="ID_844969889" CREATED="1390906240944" MODIFIED="1390906248769"/>
</node>
</node>
<node TEXT="2nd design" ID="ID_1703091221" CREATED="1391778907000" MODIFIED="1391778909781">
<node TEXT="UpdateService" STYLE_REF="Code" ID="ID_1761653400" CREATED="1391778810210" MODIFIED="1391780900027">
<node TEXT="updateDAO" ID="ID_1737685159" CREATED="1391780032553" MODIFIED="1391780036345"/>
<node TEXT="getUpdates(rootNode, timestampOfLastRequest)" ID="ID_336756389" CREATED="1391780150464" MODIFIED="1391780679656"/>
<node TEXT="subscribe(rootNode)" ID="ID_1244534959" CREATED="1391781125699" MODIFIED="1391781130671"/>
<node TEXT="unsubscribe(rootNode)" ID="ID_1785644015" CREATED="1391781131919" MODIFIED="1391781136691"/>
</node>
<node TEXT="Update" STYLE_REF="Code" ID="ID_1273168191" CREATED="1391779071880" MODIFIED="1391780900037">
<node TEXT="node:Node" ID="ID_1547926262" CREATED="1391779076330" MODIFIED="1391779301002"/>
<node TEXT="timestamp" ID="ID_829263550" CREATED="1391779078439" MODIFIED="1391779081911"/>
</node>
<node TEXT="PropertyUpdate" STYLE_REF="Code" ID="ID_659276549" CREATED="1391779084400" MODIFIED="1391780900037">
<node TEXT="key" ID="ID_816637105" CREATED="1391779089290" MODIFIED="1391779096101"/>
<node TEXT="value" ID="ID_1404352354" CREATED="1391779096390" MODIFIED="1391779097601"/>
<node TEXT="isUnset" ID="ID_1209899858" CREATED="1391779097999" MODIFIED="1391779111241"/>
</node>
<node TEXT="ChildrenUpdate" STYLE_REF="Code" ID="ID_1436671636" CREATED="1391779138101" MODIFIED="1391780900037">
<node TEXT="type:ADDED, REMOVE" ID="ID_430375678" CREATED="1391779379102" MODIFIED="1391779384603"/>
<node TEXT="targetNode:Node" ID="ID_335140311" CREATED="1391779257901" MODIFIED="1391779320862"/>
<node TEXT="targetNodeAddedBefore:Node" ID="ID_395280205" CREATED="1391779386191" MODIFIED="1391779522153"/>
</node>
<node TEXT="RootNodeProvider" STYLE_REF="Code" ID="ID_1203272361" CREATED="1391765508345" MODIFIED="1391780900047">
<node TEXT="getRootNode(Node):Node" ID="ID_1328087222" CREATED="1391765560744" MODIFIED="1391765574502"/>
</node>
<node TEXT="UpdateDAO" STYLE_REF="Code" ID="ID_1990702713" CREATED="1391779723102" MODIFIED="1391780900047">
<node TEXT="addUpdate(rootNode, update)" ID="ID_368455305" CREATED="1391779767302" MODIFIED="1391780079605"/>
</node>
<node TEXT="InMemoryUpdateDAO" STYLE_REF="Code" ID="ID_1109255745" CREATED="1391779799841" MODIFIED="1391780900047">
<node TEXT="Map&lt;Node, RootNodeInfo&gt;" ID="ID_693478277" CREATED="1391779818021" MODIFIED="1391779848133"/>
</node>
<node TEXT="RootNodeInfo" STYLE_REF="Code" ID="ID_358127371" CREATED="1391780304953" MODIFIED="1391780900047"/>
</node>
</node>
<node TEXT="Sincronizare&#xa;28/01/2014" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_1062697851" CREATED="1390907642516" MODIFIED="1390907645168">
<icon BUILTIN="group"/>
<node TEXT="design discussion" ID="ID_119800758" CREATED="1390907646954" MODIFIED="1390907650040"/>
</node>
<node TEXT="Sincronizare&#xa;07/02/2014" STYLE_REF="Validated (Green)" ID="ID_61627785" CREATED="1391778566085" MODIFIED="1391778568373">
<icon BUILTIN="group"/>
</node>
<node TEXT="Redmine" ID="ID_477598258" CREATED="1392116273505" MODIFIED="1392116273545">
<node TEXT="RM8973 - GH24 - MindMap Editor: updates (w/ server side state)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_261844590" CREATED="1392116273545" MODIFIED="1392116273565" LINK="http://csp1/redmine/issues/8973"/>
<node TEXT="RM8974 - [Review] GH24 - MindMap Editor: updates (w/ server side state)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1225124228" CREATED="1392116273895" MODIFIED="1392116273905" LINK="http://csp1/redmine/issues/8974"/>
</node>
</node>
</node>
<node TEXT="Request updates" ID="ID_345419718" CREATED="1394026236259" MODIFIED="1394027599426" LINK="#ID_716923738">
<node TEXT="For a list of resource nodes, and last update requested timestamp" ID="ID_33342210" CREATED="1394026296391" MODIFIED="1394026349452"/>
<node TEXT="We return the updates + the timestamp (of the server) + the resources that were not found (see below)." ID="ID_841271517" CREATED="1394027154842" MODIFIED="1394115817375"/>
<node TEXT="We need this information from the client even if the server knows quite well which resources the client is subscribed to. Because client = session. And the user may have 2 browser tabs (e.g. resources A+B and C+D). Without this info, we would send to both clients updates for A+B+C+D." ID="ID_1810195116" CREATED="1394026322270" MODIFIED="1394026693233"/>
<node TEXT="This is a service method, but not a remote method. This logic is automatically executed at each call (cf. below), including a dummy method in the remote service, called &quot;ping&quot;." ID="ID_250512678" CREATED="1394026696721" MODIFIED="1394026749083"/>
<node TEXT="If the client says he is subscribed to A, but he&apos;s not subscribed to A (maybe A has been closed meanwhile) =&gt; we subscribe the client to A. This may happen if a client has been offline. Probably, when he goes online, this happens for all of its resources.&#xa;&#xa;That&apos;s why, the result contains the resources that were not found. So that the client can react (i.e. close the obsolete editors)." ID="ID_5661866" CREATED="1394115538592" MODIFIED="1394115856646"/>
</node>
<node TEXT="Updates to self at the end of remote invocation" STYLE_REF="Validated (Green)" ID="ID_706078003" CREATED="1391781621833" MODIFIED="1394027599436" LINK="#ID_102227452">
<node TEXT="GH39 - Updates to self at the end of remote invocation" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_630106923" CREATED="1391781621833" MODIFIED="1393606387398" LINK="https://github.com/flower-platform/flower-platform-4/issues/39">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_192572083" CREATED="1392198516251" MODIFIED="1392198519271">
<node TEXT="RM8988 - GH39 - Updates to self at the end of remote invocation" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_30913451" CREATED="1392198516261" MODIFIED="1392198516291" LINK="http://csp1/redmine/issues/8988"/>
<node TEXT="RM8989 - [Review] GH39 - Updates to self at the end of remote invocation" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_787852011" CREATED="1392198516991" MODIFIED="1392198517001" LINK="http://csp1/redmine/issues/8989"/>
</node>
<node TEXT="FlowerJavaAdapter" ID="ID_1054075498" CREATED="1392124588095" MODIFIED="1392124588095"/>
</node>
<node TEXT="Each request from the client contains headers telling us:" ID="ID_793439418" CREATED="1394026752651" MODIFIED="1394026782473"/>
<node TEXT="a list of resource nodes, and last update requested timestamp" ID="ID_1635595130" CREATED="1394026296391" MODIFIED="1394026796643"/>
<node TEXT="So: we may request updates, and embed them in the response" ID="ID_710900504" CREATED="1394026803761" MODIFIED="1394026820943"/>
</node>
<node TEXT="GH23 - MindMap Editor: updates (w/o server side state)" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_439095064" CREATED="1389389642854" MODIFIED="1392304351770" LINK="https://github.com/flower-platform/flower-platform-4/issues/23">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_953816385" CREATED="1390904954726" MODIFIED="1390904954728">
<node TEXT="RM8909 - GH23 - MindMap Editor: updates (w/o server side state)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_672410904" CREATED="1390904954729" MODIFIED="1390904954734" LINK="http://csp1/redmine/issues/8909"/>
<node TEXT="RM8910 - [Review] GH23 - MindMap Editor: updates (w/o server side state)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1416011031" CREATED="1390904955071" MODIFIED="1390904955074" LINK="http://csp1/redmine/issues/8910"/>
</node>
<node TEXT="Expected results" ID="ID_1871699119" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="Button that invoke checkForNodeUpdates()" ID="ID_1132990188" CREATED="1390904989512" MODIFIED="1390905010675"/>
<node TEXT="checkbox: auto refresh; each 5 sec" ID="ID_595943951" CREATED="1390905013399" MODIFIED="1390905033239"/>
</node>
<node TEXT="Construction site" FOLDED="true" ID="ID_338970940" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="FreePlane impl: can we use the t/s? yes if it&apos;s the current t/s, and not the save t/s" ID="ID_164423074" CREATED="1390905666261" MODIFIED="1390905690477"/>
<node TEXT="otherwise, add in DAO" ID="ID_1653868686" CREATED="1390905698599" MODIFIED="1390905713429"/>
<node TEXT="don&apos;t forget todos; e.g.  // TODO CC: temporary code (to be refactored when update mechanism implemented)" ID="ID_1803410458" CREATED="1390932137201" MODIFIED="1390932146302"/>
<node TEXT="NodeService" STYLE_REF="Code" FOLDED="true" ID="ID_206558948" CREATED="1390905475974" MODIFIED="1390905914180">
<node TEXT="Each method that does modifications, records updates, by using NodeUpdateDAO" ID="ID_710489314" CREATED="1390905492114" MODIFIED="1390905555451"/>
<node TEXT="checkForNodeUpdates(clientNodeStatus:ClientNodeStatus):NodeUpdate" STYLE_REF="Code" ID="ID_763758833" CREATED="1390903601572" MODIFIED="1390904852515"/>
<node TEXT="Immediate updates -&gt; using proxy or ThreadLocal" STYLE_REF="New (Red)" ID="ID_514665799" CREATED="1391417732006" MODIFIED="1391417832478"/>
</node>
<node TEXT="ClientNodeStatus" STYLE_REF="Code" FOLDED="true" ID="ID_145241219" CREATED="1390903677340" MODIFIED="1390903781366">
<node TEXT="nodeId" STYLE_REF="Code" ID="ID_1988796029" CREATED="1390903744643" MODIFIED="1390903781374"/>
<node TEXT="timestamp" STYLE_REF="Code" ID="ID_1854289316" CREATED="1390903716974" MODIFIED="1390903781378"/>
<node TEXT="expandedChildren:List&lt;ClientNodeStatus&gt;" STYLE_REF="Code" ID="ID_1174244096" CREATED="1390903729054" MODIFIED="1390903781380"/>
</node>
<node TEXT="NodeUpdate" STYLE_REF="Code" FOLDED="true" ID="ID_1248903459" CREATED="1390904081999" MODIFIED="1390904855913">
<node TEXT="nodeId" STYLE_REF="Code" ID="ID_1060819460" CREATED="1390904379438" MODIFIED="1390904855916"/>
<node TEXT="updatedProperties:Map&lt;String, Object&gt;" STYLE_REF="Code" ID="ID_1446333644" CREATED="1390904088930" MODIFIED="1390904855920"/>
<node TEXT="fullChildrenList:List&lt;Node&gt;" STYLE_REF="Code" ID="ID_1067603681" CREATED="1390904629612" MODIFIED="1390904855925"/>
<node TEXT="childrenListUpdates:List&lt;ChildrenListUpdate&gt;" STYLE_REF="Code" ID="ID_1929909406" CREATED="1390904397435" MODIFIED="1390904855929"/>
<node TEXT="nodeUpdatesForChildren:List&lt;NodeUpdate&gt;" STYLE_REF="Code" ID="ID_1873552093" CREATED="1390904719294" MODIFIED="1390904855932"/>
</node>
<node TEXT="ChildrenListUpdate" STYLE_REF="Code" FOLDED="true" ID="ID_381043482" CREATED="1390904474040" MODIFIED="1390904874870">
<node TEXT="type" STYLE_REF="Code" ID="ID_692235851" CREATED="1390904477992" MODIFIED="1390904874873">
<node TEXT="ADDED, REMOVED" STYLE_REF="Code" ID="ID_1720542411" CREATED="1390904597713" MODIFIED="1390904874875"/>
</node>
<node TEXT="index" STYLE_REF="Code" ID="ID_1049697910" CREATED="1390904604826" MODIFIED="1390904874879"/>
<node TEXT="node:Node" STYLE_REF="Code" ID="ID_1143585790" CREATED="1390904610925" MODIFIED="1390904874881"/>
</node>
<node TEXT="NodeUpdateDAO" STYLE_REF="Code" ID="ID_1496166714" CREATED="1390905076919" MODIFIED="1390905559909">
<node TEXT="addPropertyUpdate(nodeId, timestamp, propertyName, propertyValue)" STYLE_REF="Code" ID="ID_1133199848" CREATED="1390905116434" MODIFIED="1390905451998"/>
<node TEXT="addChildrenListUpdate(nodeId, timestamp,  type, index, childNodeId)" STYLE_REF="Code" ID="ID_1799841461" CREATED="1390905186493" MODIFIED="1390905452015"/>
<node TEXT="getPropertyUpdates(nodeId, startingWithTimestamp)" STYLE_REF="Code" ID="ID_559795455" CREATED="1390905323737" MODIFIED="1390905452022"/>
<node TEXT="getChildrenListUpdates(nodeId, startingWithTimestamp)" STYLE_REF="Code" ID="ID_1707064852" CREATED="1390905346176" MODIFIED="1390905452025"/>
</node>
</node>
<node TEXT="Sincronizare&#xa;28/01/2014" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_788792051" CREATED="1390907639320" MODIFIED="1390907656752">
<icon BUILTIN="group"/>
<node TEXT="design discussion" ID="ID_805053711" CREATED="1390907646954" MODIFIED="1390907650040"/>
</node>
<node TEXT="Sincronizare&#xa;07/02/2014" STYLE_REF="Validated (Green)" ID="ID_1432223687" CREATED="1391765340115" MODIFIED="1391783261624">
<icon BUILTIN="group"/>
</node>
</node>
</node>
</node>
<node TEXT="Refresh subtree from client" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_406776165" CREATED="1391780959882" MODIFIED="1393606463931"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      Client tells us what are the open nodes; we send fresh data for those nodes
    </p>
  </body>
</html>
</richcontent>
<node TEXT="GH38 - Refresh subtree from client" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1807150064" CREATED="1391781023039" MODIFIED="1393606431385" LINK="https://github.com/flower-platform/flower-platform-4/issues/38">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_877709372" CREATED="1392198920047" MODIFIED="1392198920047">
<node TEXT="RM8990 - GH38 - Refresh subtree from client" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1666005236" CREATED="1392198920047" MODIFIED="1392198920077" LINK="http://csp1/redmine/issues/8990"/>
<node TEXT="RM8991 - [Review] GH38 - Refresh subtree from client" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_26666906" CREATED="1392198920397" MODIFIED="1392198920407" LINK="http://csp1/redmine/issues/8991"/>
</node>
<node TEXT="Expected results" ID="ID_773275328" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
</node>
<node TEXT="Construction site" ID="ID_1287959444" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="NodeWithVisibleChildren" ID="ID_1290286963" CREATED="1391781164301" MODIFIED="1391781389472">
<node TEXT="node" ID="ID_942324571" CREATED="1391781183679" MODIFIED="1391781191361">
<node TEXT="without properties" ID="ID_1648512854" CREATED="1391781204799" MODIFIED="1391781207851"/>
</node>
<node TEXT="visibleChildren" ID="ID_497767560" CREATED="1391781218569" MODIFIED="1391781222781"/>
</node>
<node TEXT="NodeService.refresh(nodeWithVisibleChildren):NodeWithVisibleChildren" ID="ID_274186440" CREATED="1391781226009" MODIFIED="1391781421842"/>
</node>
</node>
</node>
<node TEXT="Dynamic category based on resource type" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_1383797992" CREATED="1391777308832" MODIFIED="1393606574033" LINK="#ID_1334981843"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      Controllers based on the type of a resource, extracted form the resource id
    </p>
  </body>
</html>
</richcontent>
<node TEXT="GH37 - Dynamic category based on resource type" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_212409811" CREATED="1391777418392" MODIFIED="1393606571436" LINK="https://github.com/flower-platform/flower-platform-4/issues/37">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_582753024" CREATED="1392031574863" MODIFIED="1392031574893">
<node TEXT="RM8969 - GH37 - Dynamic category based on resource type" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_380429834" CREATED="1392031574893" MODIFIED="1392031574923" LINK="http://csp1/redmine/issues/8969"/>
<node TEXT="RM8970 - [Review] GH37 - Dynamic category based on resource type" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_10193543" CREATED="1392031577073" MODIFIED="1392031577093" LINK="http://csp1/redmine/issues/8970"/>
</node>
<node TEXT="#Expected results" ID="ID_1889830107" CREATED="1389360413465" MODIFIED="1391778030866">
<icon BUILTIN="revision"/>
<node TEXT="Dynamic category based on resource type" ID="ID_1334981843" CREATED="1391778044396" MODIFIED="1391778044426" LINK="#ID_1383797992"/>
<node TEXT="Persistence related controllers" ID="ID_1057628910" CREATED="1391778044426" MODIFIED="1391778044426" LINK="#ID_526112370"/>
</node>
<node TEXT="Construction site" ID="ID_920143093" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="TypeDescriptorRegistry.dynamicCategoryProviders" STYLE_REF="Code" ID="ID_1653044805" CREATED="1391778067185" MODIFIED="1391778442157"/>
<node TEXT="TypeDescriptor.get...Controller: add node as parameter #2" STYLE_REF="Code" ID="ID_1576740680" CREATED="1391778200646" MODIFIED="1391778442147"/>
<node TEXT="IDynamicCategoryProvider" STYLE_REF="Code" ID="ID_1618271196" CREATED="1391778084504" MODIFIED="1391778442147">
<node TEXT="getDynamicCategories(Object):List" STYLE_REF="Code" ID="ID_1530084133" CREATED="1391778091925" MODIFIED="1391778442147"/>
</node>
<node TEXT="AllDynamicCategoryProvider" STYLE_REF="Code" ID="ID_1972410163" CREATED="1391778275386" MODIFIED="1391778442137"/>
<node TEXT=".core" STYLE_REF="Code" ID="ID_17828170" CREATED="1391778296506" MODIFIED="1391778442127">
<node TEXT="ResourceTypeDynamicCategoryProvider" STYLE_REF="Code" ID="ID_1165074800" CREATED="1391778301846" MODIFIED="1391778442137">
<node TEXT="add: category.resource.???" ID="ID_273229936" CREATED="1391778320916" MODIFIED="1391778339397"/>
</node>
</node>
<node TEXT="all mm descriptors: registered for category.resource.mm" ID="ID_142265447" CREATED="1391778388596" MODIFIED="1391778403617"/>
<node TEXT="separate some basic controllers for freeplane flow" ID="ID_66312577" CREATED="1391778406127" MODIFIED="1391778418617"/>
</node>
<node TEXT="Sincronizare&#xa;07/02/2014" STYLE_REF="Validated (Green)" ID="ID_682528523" CREATED="1391778555901" MODIFIED="1391778558926">
<icon BUILTIN="group"/>
</node>
</node>
</node>
<node TEXT="File System Controllers" FOLDED="true" ID="ID_1504157521" CREATED="1391680250424" MODIFIED="1391680374368"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      Node controllers that know how to handle files. I.e.
    </p>
    <p>
      * &quot;read&quot; mode: show files, show file properties
    </p>
    <p>
      * &quot;write&quot; mode: add new file, dir, rename
    </p>
  </body>
</html>
</richcontent>
<node TEXT="GH32 - File System Controllers: first iteration" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_47685956" CREATED="1391680549626" MODIFIED="1391681492023" LINK="https://github.com/flower-platform/flower-platform-4/issues/32">
<node TEXT="Expected results" ID="ID_633218404" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
</node>
<node TEXT="Construction site" ID="ID_626781952" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="de modificat in freePlane, pentru a adauga propr isRoot" ID="ID_389303256" CREATED="1391680806837" MODIFIED="1391680828939"/>
<node TEXT="mindMapNode.isRoot -&gt; fileSystem" ID="ID_1278217750" CREATED="1391680691827" MODIFIED="1391680706229"/>
<node TEXT="fileSystem -&gt; file" ID="ID_1113081341" CREATED="1391680707477" MODIFIED="1391680715609"/>
<node TEXT="file -&gt; file" ID="ID_873021853" CREATED="1391680716327" MODIFIED="1391680720429"/>
<node TEXT="tinem in id calea fq" ID="ID_1483739841" CREATED="1391680787447" MODIFIED="1391680796139"/>
</node>
<node TEXT="Redmine" ID="ID_369339926" CREATED="1391681511293" MODIFIED="1391681511293">
<node TEXT="RM8950 - GH32 - File System Controllers: first iteration" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_642989531" CREATED="1391681511293" MODIFIED="1391681511293" LINK="http://csp1/redmine/issues/8950"/>
<node TEXT="RM8951 - [Review] GH32 - File System Controllers: first iteration" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_353192530" CREATED="1391681511643" MODIFIED="1391681511643" LINK="http://csp1/redmine/issues/8951"/>
</node>
<node TEXT="Sincronizare&#xa;13/03/2014" ID="ID_918466600" CREATED="1394722566904" MODIFIED="1394722568548">
<icon BUILTIN="group"/>
<node TEXT="Do we need a message when deleting a dir that is not empty?" ID="ID_694155803" CREATED="1392115211008" MODIFIED="1392115375534">
<node TEXT="serviciul returneaza ceva care ne-ar permite crearea unui dialog" ID="ID_919073079" CREATED="1394722691372" MODIFIED="1394722732057"/>
<node TEXT="la deschidere + OK, ar face din nou delete, dar in optiune ar pune &quot;confirm&quot;" ID="ID_291074756" CREATED="1394722732255" MODIFIED="1394722841532"/>
<node TEXT="am putea pune in descriptorul nodului, dc. necesita sau nu stergerea dialog." ID="ID_1381568720" CREATED="1394722841754" MODIFIED="1394722860449"/>
<node TEXT="Asta ar putea sa fie interpretata chiar de client direct" ID="ID_156917175" CREATED="1394722860663" MODIFIED="1394722870338"/>
</node>
<node TEXT="New tasks" ID="ID_1474071820" CREATED="1394795499070" MODIFIED="1394795501311">
<node TEXT="TODO: rename file action" ID="ID_1535237563" CREATED="1394699363303" MODIFIED="1394699394391">
<node TEXT="depindem de optiuni" ID="ID_1639868301" CREATED="1394722879582" MODIFIED="1394722883439"/>
</node>
<node TEXT="de mutat tot ce tine de mm -&gt; in proj mm" ID="ID_519970103" CREATED="1394722575338" MODIFIED="1394722587531">
<node TEXT="cateva ar ramane" ID="ID_482113755" CREATED="1394722932944" MODIFIED="1394722935845"/>
</node>
</node>
</node>
</node>
<node TEXT="GH33 - File System Controllers: use FileAccess Controller; unit test" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1592902033" CREATED="1391681393827" MODIFIED="1395223958303" LINK="https://github.com/flower-platform/flower-platform-4/issues/33">
<icon BUILTIN="flag-green"/>
<node TEXT="Expected results" ID="ID_1775030815" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="Use Plain File access controller (which was tested)" ID="ID_839286475" CREATED="1391680895697" MODIFIED="1391681453239"/>
<node TEXT="Test various operations" ID="ID_1844917329" CREATED="1391680966919" MODIFIED="1391681467759"/>
</node>
<node TEXT="Construction site" ID="ID_461567614" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
</node>
<node TEXT="Redmine" ID="ID_519686357" CREATED="1391681514413" MODIFIED="1391681514423">
<node TEXT="RM8952 - GH33 - File System Controllers: use FileAccess Controller; unit test" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_686442322" CREATED="1391681514423" MODIFIED="1391681514423" LINK="http://csp1/redmine/issues/8952"/>
<node TEXT="RM8953 - [Review] GH33 - File System Controllers: use FileAccess Controller; unit test" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1603794935" CREATED="1391681520743" MODIFIED="1391681520753" LINK="http://csp1/redmine/issues/8953"/>
</node>
</node>
</node>
<node TEXT="File Access Controller" FOLDED="true" ID="ID_1437400721" CREATED="1391681068709" MODIFIED="1391681211396"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      Abstraction mechanism that controls operations with a file. Different runtime environments have different ways of working with file: Eclipse (IFile), Idea (VirtualFile), Web (plain File)
    </p>
  </body>
</html>
</richcontent>
<node TEXT="GH31 - PlainFileAccessController" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_967837183" CREATED="1391681099312" MODIFIED="1392304351770" LINK="https://github.com/flower-platform/flower-platform-4/issues/31">
<icon BUILTIN="flag-green"/>
<node TEXT="Expected results" ID="ID_1068260297" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="Unit test:" ID="ID_1747801643" CREATED="1391681344347" MODIFIED="1391681348039"/>
<node TEXT="Hardcoded file structure" ID="ID_134683659" CREATED="1391681348517" MODIFIED="1391681353879"/>
<node TEXT="test each method" ID="ID_1955860523" CREATED="1391681354547" MODIFIED="1391681362209"/>
</node>
<node TEXT="Construction site" ID="ID_801341027" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
</node>
<node TEXT="Redmine" ID="ID_81287539" CREATED="1391681524783" MODIFIED="1391681524783">
<node TEXT="RM8954 - GH31 - PlainFileAccessController" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1813476389" CREATED="1391681524783" MODIFIED="1391681524783" LINK="http://csp1/redmine/issues/8954"/>
<node TEXT="RM8955 - [Review] GH31 - PlainFileAccessController" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_202388098" CREATED="1391681525103" MODIFIED="1391681525103" LINK="http://csp1/redmine/issues/8955"/>
</node>
</node>
</node>
<node TEXT="Remote method invocation listener" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_48532742" CREATED="1392122037405" MODIFIED="1392314835861"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      Mechanism used by the remoting backend, to give control before/after the client invokes a service method.
    </p>
  </body>
</html>
</richcontent>
<node TEXT="GH50 - Remote method invocation wrapper" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1644444044" CREATED="1392122266766" MODIFIED="1392314829824" LINK="https://github.com/flower-platform/flower-platform-4/issues/50">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_976092724" CREATED="1392223405935" MODIFIED="1392223405975">
<node TEXT="RM8994 - GH50 - Remote method invocation wrapper" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_357258564" CREATED="1392223405975" MODIFIED="1392223406005" LINK="http://csp1/redmine/issues/8994"/>
<node TEXT="RM8995 - [Review] GH50 - Remote method invocation wrapper" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1720043520" CREATED="1392223406325" MODIFIED="1392223406335" LINK="http://csp1/redmine/issues/8995"/>
</node>
<node TEXT="Expected results" ID="ID_1277342827" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
</node>
<node TEXT="Construction site" ID="ID_178048026" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="RemoteMethodInvocationListener" STYLE_REF="Code" ID="ID_1778111126" CREATED="1392125837656" MODIFIED="1392126086196">
<node TEXT="preInvoke(info)" STYLE_REF="Code" ID="ID_223824307" CREATED="1392125857636" MODIFIED="1392126095096"/>
<node TEXT="postInvoke(info)" STYLE_REF="Code" ID="ID_434899652" CREATED="1392125863276" MODIFIED="1392126095096"/>
</node>
<node TEXT="RemoteMethodInvocationInfo" STYLE_REF="Code" ID="ID_1715011823" CREATED="1392125897466" MODIFIED="1392126086206">
<node TEXT="serviceId" STYLE_REF="Code" ID="ID_1620397312" CREATED="1392125919596" MODIFIED="1392126095096"/>
<node TEXT="methodName" STYLE_REF="Code" ID="ID_1848763387" CREATED="1392125942848" MODIFIED="1392126095096"/>
<node TEXT="parameters" STYLE_REF="Code" ID="ID_762316553" CREATED="1392125946058" MODIFIED="1392126095096"/>
<node TEXT="startTimestamp" STYLE_REF="Code" ID="ID_1188798101" CREATED="1392126009474" MODIFIED="1392126095096"/>
</node>
<node TEXT="CorePlugin.remoteMethodIn..." STYLE_REF="Code" ID="ID_555320852" CREATED="1392126051794" MODIFIED="1392126086206"/>
</node>
</node>
</node>
<node TEXT="GH128 - Refactor: Group all constants" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1119578048" CREATED="1394645610271" MODIFIED="1394807837118" LINK="https://github.com/flower-platform/flower-platform-4/issues/128">
<node TEXT="each plugin: plugin name + Constants. E.g. CoreConstants" ID="ID_421771256" CREATED="1394645615822" MODIFIED="1394645650103"/>
<node TEXT="scan every constants (as, java) and group them" ID="ID_951979291" CREATED="1394645650331" MODIFIED="1394645677483"/>
<node TEXT="wiki doc" ID="ID_334863172" CREATED="1394645679671" MODIFIED="1394645682863"/>
<node TEXT="Redmine" ID="ID_1100934218" CREATED="1395844353692" MODIFIED="1395844353702">
<node TEXT="RM9217 - GH128 - Refactor: Group all constants" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1907888669" CREATED="1395844353702" MODIFIED="1395844353722" LINK="http://csp1/redmine/issues/9217"/>
<node TEXT="RM9218 - [Review] GH128 - Refactor: Group all constants" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1705238594" CREATED="1395844354282" MODIFIED="1395844354292" LINK="http://csp1/redmine/issues/9218"/>
</node>
</node>
<node TEXT="GH138 - Refactor: properties and default values" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_320277618" CREATED="1395135554317" MODIFIED="1395137339234" LINK="https://github.com/flower-platform/flower-platform-4/issues/138">
<node TEXT="Redmine" ID="ID_737111523" CREATED="1396019145280" MODIFIED="1396019145304">
<node TEXT="RM9233 - GH138 - Refactor: properties and default values" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1366732260" CREATED="1396019145305" MODIFIED="1396019145329" LINK="http://csp1/redmine/issues/9233"/>
<node TEXT="RM9234 - [Review] GH138 - Refactor: properties and default values" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1954197487" CREATED="1396019145906" MODIFIED="1396019145911" LINK="http://csp1/redmine/issues/9234"/>
</node>
<node TEXT="Node.getPropertyValue()" STYLE_REF="Code" ID="ID_1368030437" CREATED="1392813940473" MODIFIED="1392814004454">
<node TEXT="if exist in map =&gt; return" ID="ID_541927416" CREATED="1396009661114" MODIFIED="1396009670606"/>
<node TEXT="else use DPVP (below)" ID="ID_1545977559" CREATED="1396009670764" MODIFIED="1396009684046"/>
</node>
<node TEXT="additive controller" ID="ID_53322052" CREATED="1396009401270" MODIFIED="1396009420562">
<node TEXT="DefaultPropertyValueProvider.getDefaultValue(property)" STYLE_REF="Code" ID="ID_1870136175" CREATED="1396009421650" MODIFIED="1396009473632"/>
</node>
<node TEXT="for category.all" ID="ID_1828755093" CREATED="1396009478390" MODIFIED="1396009483802">
<node TEXT="PropertyDescriptorDefaultPropertyValueProvider" STYLE_REF="Code" ID="ID_1843571949" CREATED="1396009484770" MODIFIED="1396009504122"/>
</node>
<node TEXT="for MM/Styles" ID="ID_1193826596" CREATED="1396009518481" MODIFIED="1396009532173">
<node TEXT="MindMapStylesDefaultValueProvider" STYLE_REF="Code" ID="ID_253636178" CREATED="1396009533051" MODIFIED="1396009545453"/>
<node TEXT="lower orderIndex (i.e. higher priority)" ID="ID_40346911" CREATED="1396009550671" MODIFIED="1396009563543"/>
</node>
<node TEXT="for persistence controllers; PropSetter" ID="ID_508019554" CREATED="1396009694384" MODIFIED="1396009714767">
<node TEXT="compare value with default (cf. controller)" ID="ID_848838074" CREATED="1396009715425" MODIFIED="1396009725217"/>
<node TEXT="if same =&gt; unset prop" ID="ID_1385307272" CREATED="1396009725655" MODIFIED="1396009729387"/>
</node>
<node TEXT="for freePlaneMindMap" ID="ID_1961226229" CREATED="1396009935631" MODIFIED="1396009946603">
<node TEXT="IsPropertyDefaultPropertiesProvider" STYLE_REF="Code" ID="ID_1554942642" CREATED="1396009948521" MODIFIED="1396009996823">
<node TEXT="for each property" ID="ID_470617995" CREATED="1396009998981" MODIFIED="1396010017343"/>
<node TEXT="check with default" ID="ID_407119845" CREATED="1396010017561" MODIFIED="1396010021933"/>
<node TEXT="e.g. color.isPropertyDefault = true" ID="ID_1871726617" CREATED="1396010022191" MODIFIED="1396010032483"/>
</node>
</node>
<node TEXT="client/server" ID="ID_1335813605" CREATED="1392814015013" MODIFIED="1392814017574"/>
<node TEXT="deci dispare propertyValueWrapper" ID="ID_1805716869" CREATED="1392982641464" MODIFIED="1392982647665"/>
<node TEXT="&quot;Styles (read-only)&quot;" ID="ID_906261944" CREATED="1396010630327" MODIFIED="1396010638539"/>
</node>
<node TEXT="GH139 - Refactor: Add &quot;context&quot; param to NodeService methods and controllers" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_647263495" CREATED="1394638425933" MODIFIED="1395137341593" LINK="https://github.com/flower-platform/flower-platform-4/issues/139">
<node TEXT="Redmine" ID="ID_1547813715" CREATED="1395666100405" MODIFIED="1395666100415">
<node TEXT="RM9193 - GH139 - Refactor: Add &quot;context&quot; param to NodeService methods and controllers" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1577758105" CREATED="1395666100415" MODIFIED="1395666100435" LINK="http://csp1/redmine/issues/9193"/>
<node TEXT="RM9194 - [Review] GH139 - Refactor: Add &quot;context&quot; param to NodeService methods and controllers" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_946514379" CREATED="1395666100765" MODIFIED="1395666100775" LINK="http://csp1/redmine/issues/9194"/>
</node>
<node TEXT="Construction site" ID="ID_1564729219" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="initial:&#xa;toti controlleri din service, au un map de proprietati" ID="ID_1599245417" CREATED="1392814064063" MODIFIED="1394808340244">
<node TEXT="Add, set, remove CS: prelucreaza o valuare &quot;codeSyncInProgress&quot;" ID="ID_1827974174" CREATED="1392814155775" MODIFIED="1392814177276"/>
<node TEXT="Toti aditivi pot spune &quot;dontProcessOtherControllers&quot;/veto" ID="ID_1330328836" CREATED="1392814178114" MODIFIED="1392814253017"/>
<node TEXT="sa punem in el si serviciul?" ID="ID_1703251950" CREATED="1393258323408" MODIFIED="1393258329639"/>
<node TEXT="sa extindem hash map, ca sa avem acces mai simplu la serviciu?" ID="ID_1747497663" CREATED="1393258329948" MODIFIED="1393258343829"/>
</node>
<node TEXT="trebuie facute la toate" ID="ID_542896174" CREATED="1394638451353" MODIFIED="1394638459214"/>
<node TEXT="de redenumit din options in context" ID="ID_1332583518" CREATED="1394638459433" MODIFIED="1394638466364"/>
<node TEXT="sugerez sa faci testul gen: exista sau nu. Acum se pune initial un boolean = false; apoi luam booleanul si il convertim la (boolean)..." ID="ID_1968084011" CREATED="1394639747468" MODIFIED="1394639783679"/>
<node TEXT="Si la add exista pe jumatate facut. Vin de pe client, dar nu sunt transmisi mai deparete. De aici si o hardcodare referitoare la file." ID="ID_800397179" CREATED="1394797014264" MODIFIED="1394797040041"/>
<node TEXT="ar trebui si la ResourceInfoService?" ID="ID_1763212518" CREATED="1394808384568" MODIFIED="1394808471062"/>
<node TEXT="implicatii" ID="ID_1923608777" CREATED="1394808342849" MODIFIED="1394808348392">
<node TEXT="pentru codesync, va putea fi suportat si delete newly added. cf:" ID="ID_1946192440" CREATED="1393663475030" MODIFIED="1393663487461"/>
<node ID="ID_1051025706" CREATED="1393663489031" MODIFIED="1393663489031"><richcontent TYPE="NODE">

<html>
  <head>
    
  </head>
  <body>
    4) deleting a newly added node is not treated at the moment; when we implement the veto system for controllers, then we'll have the CSRemoveNodeController not prevent the persistence controller from removing a node if it was marked as added
  </body>
</html>
</richcontent>
</node>
<node TEXT="deci nu mai avem actiunea de &quot;setRemoved&quot;" ID="ID_966487306" CREATED="1392814276626" MODIFIED="1392986293479"/>
<node TEXT="in addChild: acum putem sa mutam codul specific de fisier, in controllerul fisierului; de asemenea, sa nu folosim resource" ID="ID_1996594553" CREATED="1395221701170" MODIFIED="1395228076580"/>
<node TEXT="mai erau si altele?" ID="ID_1975382745" CREATED="1395135661681" MODIFIED="1395221700271">
<node TEXT="CC: nu" ID="ID_59204020" CREATED="1395674130860" MODIFIED="1395674133938"/>
</node>
</node>
<node TEXT="todo" ID="ID_393303840" CREATED="1395674073335" MODIFIED="1395674125525">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_1827974174" STARTINCLINATION="767;0;" ENDINCLINATION="767;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_1923608777" STARTINCLINATION="102;0;" ENDINCLINATION="102;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
<node TEXT="Sincronizare&#xa;16/04/2014" ID="ID_687141303" CREATED="1397652257884" MODIFIED="1397652259224">
<icon BUILTIN="group"/>
<node TEXT="ServiceContext: sa contina map" ID="ID_1025465717" CREATED="1397652261063" MODIFIED="1397652270158"/>
<node TEXT="getValue -&gt; getBooleanValue" ID="ID_1442000978" CREATED="1397652270396" MODIFIED="1397652290476"/>
<node TEXT="acest map primibil de pe flex" ID="ID_1882900630" CREATED="1397652291434" MODIFIED="1397652301170"/>
<node TEXT="in ServiceContext: de tinut ref catre serviciu: =&gt; ServiceContext&lt;T&gt;" ID="ID_607966314" CREATED="1397652320416" MODIFIED="1397652350279"/>
<node TEXT="as propune: pe unde e folosit getInstance, sa folosim aceasta noua informatie. Pentru NodeS si ResS =&gt; de avut referinta unul la altele. Mai sunt si altele?" ID="ID_938812757" CREATED="1397652414493" MODIFIED="1397652455130"/>
</node>
</node>
</node>
<node TEXT="GH140 - Refactor: IDescriptor, GenericDescriptor and copy descriptors to client" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1176383195" CREATED="1392813931323" MODIFIED="1395137343974" LINK="https://github.com/flower-platform/flower-platform-4/issues/140">
<font BOLD="false"/>
<node TEXT="transportare toti descriptorii pe flex" ID="ID_189800477" CREATED="1393873754359" MODIFIED="1393873763611">
<node TEXT="probabil printr-o interfata marker" ID="ID_1845098978" CREATED="1393873764522" MODIFIED="1393873771980">
<node TEXT="IDescriptor" STYLE_REF="Code" ID="ID_1892909097" CREATED="1395157097303" MODIFIED="1395157101435"/>
</node>
<node TEXT="e nevoie la: props, add child, etc" ID="ID_39702676" CREATED="1393873787915" MODIFIED="1393873797276"/>
</node>
<node TEXT="descriptor de nod" ID="ID_727214451" CREATED="1393934718582" MODIFIED="1393934722373">
<node TEXT="proprietate de titlu" ID="ID_555199917" CREATED="1393934723142" MODIFIED="1393934729853"/>
<node TEXT="proprietate de iconita" ID="ID_1850598128" CREATED="1393934730132" MODIFIED="1393934736783"/>
<node TEXT="GenericDescriptor" STYLE_REF="Code" ID="ID_1041995775" CREATED="1395135952954" MODIFIED="1395136437533">
<node TEXT="value:Object" STYLE_REF="Code" ID="ID_375743248" CREATED="1395136426853" MODIFIED="1395136438296"/>
<node TEXT="inregistrat pe diverse chestii. E.g. propertyForTitleDescriptor, propertyForIconDescriptor, etc" ID="ID_84869839" CREATED="1395136440078" MODIFIED="1395136490075"/>
</node>
<node TEXT="pe client" ID="ID_1616588154" CREATED="1395137514696" MODIFIED="1395137522446"/>
<node TEXT="IGenericValueProvider" STYLE_REF="Code" ID="ID_621856118" CREATED="1395137523625" MODIFIED="1395137622202">
<node TEXT="getValue(node:Node):Object" STYLE_REF="Code" ID="ID_1226397612" CREATED="1395137563081" MODIFIED="1395137624550"/>
</node>
<node TEXT="GenericDescriptorValueProvider" STYLE_REF="Code" ID="ID_738760846" CREATED="1395137578895" MODIFIED="1395137622196">
<node TEXT="genericDescriptorName:String" ID="ID_542280067" CREATED="1395137601415" MODIFIED="1395137615059"/>
</node>
<node TEXT="astea stau in registrul de flex. E.g. nodeTitleProvider. Pe all inregistrez un GDVP(propertyForTitleDescriptor)" ID="ID_1759181656" CREATED="1395157724940" MODIFIED="1395157816990"/>
<node TEXT="folosit sistemul asta pentru modul de decorare al claselor CodeSync" ID="ID_649685054" CREATED="1395158006628" MODIFIED="1395158018690"/>
</node>
<node TEXT="Redmine" ID="ID_1153432030" CREATED="1395158092937" MODIFIED="1395158092946">
<node TEXT="RM9156 - GH140 - Refactor: IDescriptor, GenericDescriptor and copy descriptors to client" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1017074863" CREATED="1395158092948" MODIFIED="1395158092990" LINK="http://csp1/redmine/issues/9156"/>
<node TEXT="RM9157 - [Review] GH140 - Refactor: IDescriptor, GenericDescriptor and copy descriptors to client" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_133931279" CREATED="1395158093343" MODIFIED="1395158093353" LINK="http://csp1/redmine/issues/9157"/>
</node>
</node>
<node TEXT="Configuration Properties" STYLE_REF="Done 2 (Light Blue)" FOLDED="true" ID="ID_954464860" CREATED="1395232217599" MODIFIED="1398671193991"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      Framework for defining properties (i.e. that stay in .properties file).
    </p>
  </body>
</html>
</richcontent>
<node TEXT="in META-INF" ID="ID_1103027273" CREATED="1397657349680" MODIFIED="1397657362534"/>
<node TEXT="flower-platform.properties" ID="ID_641205479" CREATED="1397657363892" MODIFIED="1397657371736"/>
<node TEXT="flower-platform.properties.local" ID="ID_174679936" CREATED="1397657373444" MODIFIED="1397657722315">
<node TEXT="To override default props" ID="ID_1080754952" CREATED="1397657390417" MODIFIED="1397657397651"/>
</node>
<node TEXT="GH153 - Reintroduce the properties framework" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1302583264" CREATED="1394642720686" MODIFIED="1395232287980" LINK="https://github.com/flower-platform/flower-platform-4/issues/153">
<node TEXT="wiki" ID="ID_166040401" CREATED="1394642727817" MODIFIED="1394642728918"/>
<node TEXT="sebastian needed it" ID="ID_635527980" CREATED="1394642729146" MODIFIED="1394642733418"/>
<node TEXT="Mariana as well, in resource unsubscriber" ID="ID_1600038254" CREATED="1394642733627" MODIFIED="1395232079349"/>
<node TEXT="need a property for a margin for the request updates mechanism; if client timestamp + MARGIN &lt; first update registered for resource =&gt; send null" ID="ID_80176451" CREATED="1395764474933" MODIFIED="1395764642780"/>
<node TEXT="Redmine" ID="ID_1294181251" CREATED="1396515160646" MODIFIED="1396515160666">
<node TEXT="RM9255 - GH153 - Reintroduce the properties framework" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_746899284" CREATED="1396515160668" MODIFIED="1396515160694" LINK="http://csp1/redmine/issues/9255"/>
<node TEXT="RM9256 - [Review] GH153 - Reintroduce the properties framework" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1272577610" CREATED="1396515161301" MODIFIED="1396515161309" LINK="http://csp1/redmine/issues/9256"/>
</node>
<node TEXT="Sincronizare&#xa;16/04/2014" ID="ID_1185755778" CREATED="1397656934179" MODIFIED="1397656937289">
<icon BUILTIN="group"/>
<node TEXT="some comments need to be updated (SVN, .override...)" ID="ID_62538671" CREATED="1397656938477" MODIFIED="1397656969365"/>
<node TEXT="comments for all properties" ID="ID_450775706" CREATED="1397656969583" MODIFIED="1397656987099"/>
</node>
</node>
</node>
<node TEXT="Log" STYLE_REF="Resubmitted (Violet)" FOLDED="true" ID="ID_213999718" CREATED="1397657654959" MODIFIED="1397657731127" VSHIFT="10">
<node TEXT="in META-INF" ID="ID_1501241624" CREATED="1397657349680" MODIFIED="1397657362534"/>
<node TEXT="logback.config" ID="ID_1447593743" CREATED="1397657679440" MODIFIED="1397657688933"/>
<node TEXT="logback.config.local" ID="ID_1761314210" CREATED="1397657689271" MODIFIED="1397657708957"/>
<node TEXT="GH48 - Logging configuration" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_959439497" CREATED="1392120550452" MODIFIED="1392120863653" LINK="https://github.com/flower-platform/flower-platform-4/issues/48">
<node TEXT="Put somewhere the logback config file" ID="ID_248342939" CREATED="1392120569832" MODIFIED="1392120579843"/>
<node TEXT="Why not, reenable the color system" ID="ID_1659530641" CREATED="1392120580372" MODIFIED="1392120592753"/>
<node TEXT="Where will the log file be? In FP2 we had something specific to put in a dir relative to the context" ID="ID_669421444" CREATED="1392120802322" MODIFIED="1392120835403"/>
<node TEXT="Where will it be in desktop?" ID="ID_1426093546" CREATED="1392120835781" MODIFIED="1392120842593"/>
<node TEXT="Update the wiki page" ID="ID_1596428732" CREATED="1392120875012" MODIFIED="1392120877823"/>
<node TEXT="CC: done in #153" ID="ID_759801128" CREATED="1396602391273" MODIFIED="1396602396055"/>
<node TEXT="Sincronizare&#xa;16/04/2014" ID="ID_1354230248" CREATED="1397656671979" MODIFIED="1397656673469">
<icon BUILTIN="group"/>
<node TEXT="Sa incercam sa luam din 2 locuri configuratia. Pe sistemul ca a 2-a ar fi o chestie locala. As propune .core/META-INF/logback-local.config" ID="ID_319269014" CREATED="1397656678948" MODIFIED="1397656724131"/>
<node TEXT="Asta ar insemna ca am putea poate sa mutam si din .util fisierul. Cred ca trebuie sa specificam programatic de unde sa-l ia" ID="ID_1416449324" CREATED="1397656730160" MODIFIED="1397656819291"/>
</node>
<node TEXT="http://logback.qos.ch/manual/configuration.html#logback.statusLC" ID="ID_92027983" CREATED="1397814953723" MODIFIED="1397814953723" LINK="http://logback.qos.ch/manual/configuration.html#logback.statusLC"/>
</node>
</node>
<node TEXT="Debug controllers for TypeDescriptorRegistry" STYLE_REF="New (Red)" FOLDED="true" ID="ID_297168335" CREATED="1395233993163" MODIFIED="1395234320776">
<node TEXT="GH159 - Debug controllers for TypeDescriptorRegistry" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1384025135" CREATED="1395233993163" MODIFIED="1397039772465" LINK="https://github.com/flower-platform/flower-platform-4/issues/159">
<node TEXT="e.g." ID="ID_1193868996" CREATED="1395234080976" MODIFIED="1395234086724">
<node TEXT="Node Types" ID="ID_1771994048" CREATED="1396434171509" MODIFIED="1396434190515">
<node TEXT="Flex" ID="ID_98525557" CREATED="1395234035615" MODIFIED="1395234037293"/>
<node TEXT="Java" ID="ID_151203959" CREATED="1395234037530" MODIFIED="1395234038861">
<node TEXT="myType" ID="ID_1108155346" CREATED="1395234087802" MODIFIED="1396433975752">
<icon BUILTIN="idea"/>
<node TEXT="category.all" ID="ID_1130553247" CREATED="1395234098673" MODIFIED="1395234169665">
<icon BUILTIN="folder"/>
</node>
<node TEXT="category.myCategory" ID="ID_923182073" CREATED="1395234104908" MODIFIED="1395234169668">
<icon BUILTIN="folder"/>
</node>
<node TEXT="controllerKeySingle" ID="ID_912038252" CREATED="1396433467583" MODIFIED="1396433673038">
<icon BUILTIN="full-1"/>
<node TEXT="controller" ID="ID_1645455623" CREATED="1396433532814" MODIFIED="1396433764302">
<icon BUILTIN="executable"/>
<icon BUILTIN="attach"/>
</node>
<node TEXT="overriddenController" ID="ID_1700154281" CREATED="1396433619249" MODIFIED="1396433660159">
<icon BUILTIN="button_cancel"/>
</node>
</node>
<node TEXT="controllerKeySingle" ID="ID_49581648" CREATED="1396433467583" MODIFIED="1396433673038">
<icon BUILTIN="full-1"/>
<node TEXT="controller" ID="ID_642932946" CREATED="1396433532814" MODIFIED="1396433776341">
<icon BUILTIN="executable"/>
</node>
</node>
<node TEXT="controllerKeyAdditive" ID="ID_224566634" CREATED="1396433516187" MODIFIED="1396433697966">
<icon BUILTIN="positive"/>
<node TEXT="contr1" ID="ID_437483894" CREATED="1396433541415" MODIFIED="1396433709092">
<icon BUILTIN="executable"/>
</node>
<node TEXT="contr2" ID="ID_550851096" CREATED="1396433544206" MODIFIED="1396433709095">
<icon BUILTIN="executable"/>
</node>
<node TEXT="contr3Contributed" ID="ID_548890191" CREATED="1396433561306" MODIFIED="1396433758389">
<icon BUILTIN="executable"/>
<icon BUILTIN="attach"/>
</node>
</node>
</node>
</node>
</node>
</node>
<node TEXT="Redmine" ID="ID_727346869" CREATED="1396427446452" MODIFIED="1396427446458">
<node TEXT="RM9247 - GH159 - Debug controllers for TypeDescriptorRegistry" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1786576804" CREATED="1396427446460" MODIFIED="1396427446484" LINK="http://csp1/redmine/issues/9247"/>
<node TEXT="RM9248 - [Review] GH159 - Debug controllers for TypeDescriptorRegistry" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_156188900" CREATED="1396427446810" MODIFIED="1396427446813" LINK="http://csp1/redmine/issues/9248"/>
</node>
</node>
</node>
<node TEXT="Login and Authentication" STYLE_REF="Planned (Orange)" FOLDED="true" ID="ID_703509399" CREATED="1395326245475" MODIFIED="1396438564024">
<node TEXT="GH192 - Analysis: Login and Authentication" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_967696793" CREATED="1395326245475" MODIFIED="1396438561194" LINK="https://github.com/flower-platform/flower-platform-4/issues/192">
<node TEXT="HTML Login window" ID="ID_1056077112" CREATED="1395325999706" MODIFIED="1395326266467">
<node TEXT="Runnable as stand-alone as well?&#xa;i.e. redirect after login to main.jsp" ID="ID_1172404552" CREATED="1395326297886" MODIFIED="1395326315947"/>
</node>
<node TEXT="Remember me&#xa;(based on cookie)" ID="ID_1565949930" CREATED="1395326319605" MODIFIED="1395326352137">
<node TEXT="cred ca func astfel:" ID="ID_1168217127" CREATED="1396434992724" MODIFIED="1396434999664"/>
<node TEXT="se foloseste un cookie de interval lung" ID="ID_597702606" CREATED="1396435000007" MODIFIED="1396435012205"/>
<node TEXT="cand ma conectez, il folosesc si sunt intr-o stare &quot;logat semi-safe&quot;" ID="ID_977936233" CREATED="1396435012545" MODIFIED="1396435026529"/>
<node TEXT="anumite operatiuni, au nevoie de &quot;logat safe&quot; =&gt; fereastra de parola" ID="ID_343530116" CREATED="1396435026744" MODIFIED="1396435045668"/>
<node TEXT="aceasta stare &quot;logat safe&quot; dureaza per sesiune" ID="ID_1566396025" CREATED="1396435045923" MODIFIED="1396435055500"/>
</node>
<node TEXT="Authentication: both Flex and HTML (for embeded HTML windows)" ID="ID_296982788" CREATED="1395326352455" MODIFIED="1395326424708"/>
<node TEXT="Idem for mobile" ID="ID_100342070" CREATED="1395326450557" MODIFIED="1395326467328">
<node TEXT="sa nu tinem parola in clar?" ID="ID_1779588378" CREATED="1396435220234" MODIFIED="1396435294735"/>
<node TEXT="i.e. logare de pe aplicatia flex; si din paginile html embeded" ID="ID_950272041" CREATED="1396435371197" MODIFIED="1396435384536"/>
</node>
<node TEXT="Server: delegate to permissions system: may allow for anyone (including anonymous), only for a logged users or only for a certain role" ID="ID_1675449067" CREATED="1395326926817" MODIFIED="1395326982029">
<node TEXT="403/unauth" ID="ID_1160699164" CREATED="1396435452648" MODIFIED="1396435456142"/>
<node TEXT="login required" ID="ID_1639950119" CREATED="1396435456506" MODIFIED="1396435462005"/>
</node>
<node TEXT="When no longer authorized =&gt; the login window pops up" ID="ID_1423430940" CREATED="1395326478936" MODIFIED="1395326500958">
<node TEXT="Can be used as well on first connect. No auth =&gt; window" ID="ID_1526871025" CREATED="1395326501566" MODIFIED="1395326588568"/>
</node>
<node TEXT="Option: is login mandatory?" ID="ID_422779539" CREATED="1395326782977" MODIFIED="1395326862589">
<node TEXT="Maybe put it in the &quot;Pro&quot; version" ID="ID_164290817" CREATED="1395326862997" MODIFIED="1395326874459"/>
</node>
<node TEXT="Atentie: acum am facut un fisier: mobile-host-check-helper.html. Asta e folosit de mobil, sa detecteze daca URL-ul pointeaza catre un server valid. In noua configuratie, probabil ca vom renunta la acest sistem, in favoarea a sistemului de login." ID="ID_515904999" CREATED="1396967444046" MODIFIED="1396967631917"/>
</node>
</node>
</node>
<node TEXT="desktop" POSITION="right" ID="ID_1614176340" CREATED="1390480507509" MODIFIED="1390480509751"/>
<node TEXT="desktop.eclipse" FOLDED="true" POSITION="right" ID="ID_535444190" CREATED="1390405899849" MODIFIED="1390480504121">
<node TEXT="Construction site" ID="ID_1651071267" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="luate din FP3" STYLE_REF="In progress (Yellow)" ID="ID_816116718" CREATED="1375801262459" MODIFIED="1390557770470">
<node TEXT="Limitations" STYLE_REF="Features node (Blue cloud)" ID="ID_502310006" CREATED="1380050321142" MODIFIED="1380051740156">
<font BOLD="true"/>
<node TEXT="On Windows, using the default embeded web browser (IE), communication hangs when opening the second browser with a streaming connection." FOLDED="true" ID="ID_1528942151" CREATED="1380050326071" MODIFIED="1380050396836">
<node TEXT="All sorts of hacks have been tried. Including embeding the Flash Player ActiveX directly into the editor." ID="ID_1159642874" CREATED="1380050397444" MODIFIED="1380050427233"/>
<node TEXT="Has been solved using XULRunner (mozilla)" ID="ID_1636677536" CREATED="1380050427692" MODIFIED="1380050442132"/>
</node>
<node TEXT="Memory problems: after opening a certain number of browsers + app (between 5 - 10), Eclipse stops abruptly." ID="ID_106139237" CREATED="1380050482754" MODIFIED="1380050522986">
<node TEXT="Folosind -Xmx512m in loc de -Xmx1024m, merge mult mai bine (putem deschide mai multe tab-uri, nu avem limitare de memorie, dar pe la al 16-lea tab al unei app simple, tot crapa)" ID="ID_835902846" CREATED="1367592601094" MODIFIED="1367592720032"/>
<node TEXT="https://bugs.eclipse.org/bugs/show_bug.cgi?id=290132" ID="ID_947139322" CREATED="1368457856120" MODIFIED="1368774170317" LINK="https://bugs.eclipse.org/bugs/show_bug.cgi?id=290132">
<icon BUILTIN="messagebox_warning"/>
</node>
<node TEXT="http://helpx.adobe.com/flash-builder/kb/sluggish-performance-out-memory-errors.html" ID="ID_1089170243" CREATED="1368774424128" MODIFIED="1368774459229" LINK="http://helpx.adobe.com/flash-builder/kb/sluggish-performance-out-memory-errors.html">
<icon BUILTIN="messagebox_warning"/>
</node>
</node>
</node>
<node TEXT="Improvements" STYLE_REF="Features node (Blue cloud)" ID="ID_1416574532" CREATED="1380046538176" MODIFIED="1380051743642">
<font BOLD="true"/>
<node TEXT="Several editors may reuse the same browser" FOLDED="true" ID="ID_268299908" CREATED="1380047443142" MODIFIED="1380050606364">
<node TEXT="E.g. we have 3 open editors, but only a single browser. When one editor (tab) becomes active =&gt; the browser component is relocated from the old editor to the new one. If there are multiple editor spaces, e.g. 2, there should be 2 browsers, etc." ID="ID_806789867" CREATED="1380050607311" MODIFIED="1380050612516"/>
<node TEXT="This would increase the number of simultaneous editors." ID="ID_574221523" CREATED="1380050613022" MODIFIED="1380050614070"/>
<node TEXT="Radio button setting: Open a new diagram: (x) in a reused editor () in a new editor." STYLE_REF="New (Red)" ID="ID_1384182940" CREATED="1380047205340" MODIFIED="1380047316861"/>
<node TEXT="Moving the browser: I remember that a couple of years ago, this didn&apos;t work on Mac. Another solution would be to move the whole editor (i.e. change it&apos;s tab order) over a dummy/empty editor." ID="ID_435403968" CREATED="1380050617330" MODIFIED="1380050700653"/>
</node>
</node>
<node TEXT="Features" STYLE_REF="Features node (Blue cloud)" ID="ID_1997673050" CREATED="1380051751345" MODIFIED="1380051782745">
<node TEXT="Editor with embeded browser" STYLE_REF="In progress (Yellow)" ID="ID_1713872792" CREATED="1380046343507" MODIFIED="1380047162145">
<node TEXT="Close editor =&gt; force the unsubscription from the open resources (so that a streaming channel may be reused immediately" STYLE_REF="New (Red)" ID="ID_1021727771" CREATED="1380046820030" MODIFIED="1380047156481"/>
</node>
<node TEXT="Dirty and saveables" STYLE_REF="New (Red)" ID="ID_1921851030" CREATED="1380050742188" MODIFIED="1380050751585">
<node TEXT="GH126" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1559007949" CREATED="1380119785454" MODIFIED="1380120905549"/>
</node>
<node TEXT="Add elements on diagram" STYLE_REF="New (Red)" ID="ID_7017513" CREATED="1380725143064" MODIFIED="1380725221861">
<node TEXT="Native drag and drop from tree to diagram" STYLE_REF="New (Red)" ID="ID_1258608036" CREATED="1380050913902" MODIFIED="1380051542483">
<node TEXT="Win" STYLE_REF="New (Red)" ID="ID_395083809" CREATED="1380050929640" MODIFIED="1380051542491"/>
<node TEXT="Mac" STYLE_REF="New (Red)" ID="ID_95561880" CREATED="1380050931231" MODIFIED="1380051542495"/>
<node TEXT="Linux" STYLE_REF="New (Red)" ID="ID_810525022" CREATED="1380050932469" MODIFIED="1380051542498"/>
<node TEXT="GH127" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_648324174" CREATED="1380119839788" MODIFIED="1380120925863"/>
</node>
<node TEXT="Action" STYLE_REF="Planned (Orange)" ID="ID_1406920218" CREATED="1380725225298" MODIFIED="1380725232455">
<node TEXT="GH168 - Action that adds elements on diagram" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1857853602" CREATED="1380725152487" MODIFIED="1380725167060" LINK="https://github.com/flower-platform/main/issues/168"/>
</node>
</node>
<node TEXT="Popup for alerts" STYLE_REF="New (Red)" FOLDED="true" ID="ID_1916716554" CREATED="1380051068330" MODIFIED="1380051542501">
<node TEXT="Including an &quot;on the fly&quot; properties editor" ID="ID_554784466" CREATED="1380051295375" MODIFIED="1380051304719"/>
</node>
<node TEXT="Selection synchronization between editors/views" STYLE_REF="New (Red)" ID="ID_1253383451" CREATED="1380051391981" MODIFIED="1380051542505"/>
<node TEXT="GH128" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1063059427" CREATED="1380120887949" MODIFIED="1380120925860"/>
<node TEXT="Decorate files in tree(s) based on CodeSync markers" STYLE_REF="New (Red)" ID="ID_1466357433" CREATED="1376489268062" MODIFIED="1380051542509">
<node TEXT="GH68" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_53802215" CREATED="1376489326708" MODIFIED="1376490838888">
<node TEXT="Investigations" STYLE_REF="Planned (Orange)" FOLDED="true" ID="ID_1362380375" CREATED="1376489359164" MODIFIED="1376489821391">
<node TEXT="Decorate any file with an codesync icon" ID="ID_1720327584" CREATED="1376489338524" MODIFIED="1376489797521"/>
<node TEXT="inspiration: NavigatorClassLabelDecorator" ID="ID_373171184" CREATED="1376489798204" MODIFIED="1376489815129"/>
</node>
</node>
<node TEXT="GH107" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_898469340" CREATED="1380535710829" MODIFIED="1380535719115"/>
</node>
<node TEXT="Connect to Flower Platform server and Login" STYLE_REF="New (Red)" ID="ID_678271956" CREATED="1380119566109" MODIFIED="1380119603959">
<node TEXT="GH123" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_559728207" CREATED="1380119625825" MODIFIED="1380120925857"/>
</node>
<node TEXT="Collaborate" STYLE_REF="New (Red)" ID="ID_1424258886" CREATED="1380119700747" MODIFIED="1380119717039">
<node TEXT="GH125" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_310753390" CREATED="1380119746879" MODIFIED="1380120925853"/>
</node>
<node TEXT="Create New Diagram action" STYLE_REF="Planned (Orange)" ID="ID_181393961" CREATED="1380723724282" MODIFIED="1380723757893">
<node TEXT="GH163 - Create New Diagram action" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1725572488" CREATED="1380723724282" MODIFIED="1380723739752" LINK="https://github.com/flower-platform/main/issues/163"/>
</node>
</node>
<node TEXT="GH52" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1157841292" CREATED="1375801268287" MODIFIED="1375801494749">
<node TEXT="editor that wraps a browser, registered on a dummy extension" STYLE_REF="Done (Blue)" ID="ID_494382815" CREATED="1375801527675" MODIFIED="1375966333387"/>
<node TEXT="start jetty server; display the current diagram (from the file)" STYLE_REF="Done (Blue)" ID="ID_575231416" CREATED="1375801566159" MODIFIED="1376464046223"/>
<node TEXT="Sincronizare&#xa;14/08/2013" STYLE_REF="Validated (Green)" ID="ID_1266511685" CREATED="1375801645971" MODIFIED="1376489826880">
<icon BUILTIN="group"/>
</node>
<node TEXT="solve classpath problms" STYLE_REF="New (Red)" ID="ID_226646662" CREATED="1376489827765" MODIFIED="1376489840057"/>
</node>
</node>
</node>
<node TEXT="Editor with embeded browser" STYLE_REF="Done (Blue)" ID="ID_258082754" CREATED="1390405963916" MODIFIED="1390559025413">
<icon BUILTIN="flag"/>
<node TEXT="If the resource is already open in an editor =&gt; reveal editor" STYLE_REF="Done (Blue)" ID="ID_1702397609" CREATED="1380050201324" MODIFIED="1390559005077"/>
<node TEXT="Editor reuse logic" STYLE_REF="New (Red)" ID="ID_1714063277" CREATED="1390406233688" MODIFIED="1390558178617">
<node TEXT=".=&gt; an editor that embeds the browser is opened and the diagram is shown" ID="ID_1711858051" CREATED="1380046978361" MODIFIED="1380046986202"/>
<node TEXT="If no editors exist, a new editor is created" ID="ID_634830608" CREATED="1380046993698" MODIFIED="1380047011114"/>
<node TEXT="If editors exist, reuse the last one that was active. (A click on it&apos;s tab =&gt; we consider it was active.)" ID="ID_474281650" CREATED="1380047011463" MODIFIED="1380047106556"/>
<node TEXT="If the chosen editor is not on top: bring it to top and set focus." ID="ID_1330175649" CREATED="1380047337454" MODIFIED="1380047354502"/>
</node>
<node TEXT="Open (in New Editor)" STYLE_REF="New (Red)" ID="ID_1335606679" CREATED="1380047178063" MODIFIED="1380047199986"/>
<node TEXT="GH11 - Eclipse project, first iteration" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_18641383" CREATED="1390406375892" MODIFIED="1390406413114" LINK="https://github.com/flower-platform/flower-platform-4/issues/11">
<node TEXT="Redmine" ID="ID_925155568" CREATED="1390406916304" MODIFIED="1390406916306">
<node TEXT="RM8852 - GH11 - Eclipse project, first iteration" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_244586952" CREATED="1390406916306" MODIFIED="1390406916311" LINK="http://csp1/redmine/issues/8852"/>
<node TEXT="RM8853 - [Review] GH11 - Eclipse project, first iteration" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1146189861" CREATED="1390406916526" MODIFIED="1390406916532" LINK="http://csp1/redmine/issues/8853"/>
</node>
<node TEXT="Expected results" ID="ID_1397678412" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT=".jetty, starts the server" ID="ID_987487646" CREATED="1390406429433" MODIFIED="1390406472239"/>
<node TEXT="editor" ID="ID_1772250436" CREATED="1390406475778" MODIFIED="1390406480562"/>
<node TEXT="menu action that starts editor" ID="ID_303833510" CREATED="1390406480808" MODIFIED="1390406808305"/>
<node TEXT="editor for .flower-diagram" ID="ID_492767624" CREATED="1390406808534" MODIFIED="1390406822653"/>
</node>
</node>
</node>
<node TEXT="Flower Platform menu" STYLE_REF="Done (Blue)" ID="ID_884441900" CREATED="1390406009695" MODIFIED="1390559005068">
<node TEXT="Open Flower Platform Tree" STYLE_REF="Done (Blue)" ID="ID_143329053" CREATED="1390406017228" MODIFIED="1390559005075"/>
</node>
<node TEXT="Preference page" STYLE_REF="Done (Blue)" ID="ID_251977366" CREATED="1380046721854" MODIFIED="1390835582984">
<node TEXT="FieldSet: Flower Platform Editors" STYLE_REF="Done (Blue)" ID="ID_1903704425" CREATED="1390562753040" MODIFIED="1390835614694">
<node TEXT="Explanation" STYLE_REF="Done (Blue)" ID="ID_1902298727" CREATED="1390562816509" MODIFIED="1390835617433" LINK="#ID_1482985338">
<node TEXT="The Flower Platform editor uses an embeded browser, which contains an embeded Flash (Flex) app. It&apos;s recommended to limit the number of open editors, in order to limit resources usage and improve performance and stability." ID="ID_153115178" CREATED="1390558447115" MODIFIED="1390558584187"/>
</node>
<node TEXT="Open editor behavior" STYLE_REF="Done (Blue)" ID="ID_273609609" CREATED="1390558312845" MODIFIED="1390835618403" LINK="#ID_1828014273">
<node TEXT="Radio with values" ID="ID_1271642751" CREATED="1390558384125" MODIFIED="1390558391867">
<node TEXT="Reuse an existing editor (if possible)" ID="ID_1232623139" CREATED="1390558393355" MODIFIED="1390558427547">
<node TEXT="default" ID="ID_1155662065" CREATED="1390562614609" MODIFIED="1390562616001"/>
</node>
<node TEXT="Open a new editor" ID="ID_755753424" CREATED="1390558416085" MODIFIED="1390558442217"/>
</node>
</node>
<node TEXT="Maximum number of simultaneous editors. When this limit is reached, new editors will always reuse an existing editor." STYLE_REF="Done (Blue)" ID="ID_714883770" CREATED="1380046727061" MODIFIED="1390835619423" LINK="#ID_1170554218">
<node TEXT="Default to 5" ID="ID_894369719" CREATED="1390558657538" MODIFIED="1390558657538"/>
<node TEXT="When this number is reached, all editor opening, will reuse the logic, regardless of the action (e.g. Open in new editor), or of the default open behavior" ID="ID_320282485" CREATED="1390558215446" MODIFIED="1390558309957"/>
</node>
</node>
<node TEXT="GH14 - Preference page" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_654780809" CREATED="1390558879058" MODIFIED="1390558975860" LINK="https://github.com/flower-platform/flower-platform-4/issues/14">
<node TEXT="Redmine" ID="ID_1682241899" CREATED="1390580009885" MODIFIED="1390580009905">
<node TEXT="RM8873 - GH14 - Preference page" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1472348016" CREATED="1390580009905" MODIFIED="1390580009945" LINK="http://csp1/redmine/issues/8873"/>
<node TEXT="RM8874 - [Review] GH14 - Preference page" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_642170216" CREATED="1390580010265" MODIFIED="1390580010265" LINK="http://csp1/redmine/issues/8874"/>
</node>
<node TEXT="#Expected results" ID="ID_546053439" CREATED="1389360413465" MODIFIED="1390558909010">
<icon BUILTIN="revision"/>
<node TEXT="Explanation" ID="ID_1482985338" CREATED="1390562865581" MODIFIED="1390562865591" LINK="#ID_1902298727"/>
<node TEXT="Open editor behavior" ID="ID_1828014273" CREATED="1390558936390" MODIFIED="1390558936400" LINK="#ID_273609609"/>
<node TEXT="Maximum number of simultaneous editors. When this limit is reached, new editors will always reuse an existing editor." ID="ID_1170554218" CREATED="1390558936360" MODIFIED="1390558936380" LINK="#ID_714883770"/>
</node>
</node>
</node>
<node TEXT="Flower Platform Properties view" STYLE_REF="Done (Blue)" FOLDED="true" ID="ID_1586218773" CREATED="1390406153569" MODIFIED="1390919676825">
<node TEXT="GH15 - Flower Platform Properties view" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1410977392" CREATED="1390406153569" MODIFIED="1390919652875" LINK="https://github.com/flower-platform/flower-platform-4/issues/15">
<node TEXT="Redmine" ID="ID_1660197521" CREATED="1390835462048" MODIFIED="1390835462078">
<node TEXT="RM8904 - GH15 - Flower Platform Properties view" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_74715958" CREATED="1390835462078" MODIFIED="1390835462098" LINK="http://csp1/redmine/issues/8904"/>
<node TEXT="RM8905 - [Review] GH15 - Flower Platform Properties view" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1317800776" CREATED="1390835467618" MODIFIED="1390835467628" LINK="http://csp1/redmine/issues/8905"/>
</node>
<node TEXT="#Expected results" ID="ID_965903547" CREATED="1389360413465" MODIFIED="1390558909010">
<icon BUILTIN="revision"/>
<node TEXT="Generic mechanism to be able to easily define new view." ID="ID_302468404" CREATED="1390562413028" MODIFIED="1390562442040"/>
<node TEXT="Reuse as much logic as possilbe with the &quot;open editor&quot;" ID="ID_1798302138" CREATED="1390562904232" MODIFIED="1390562922288"/>
</node>
</node>
</node>
</node>
<node TEXT="desktop.idea" FOLDED="true" POSITION="right" ID="ID_1501675561" CREATED="1390406528620" MODIFIED="1390480496099">
<node TEXT="Idem eclipse" STYLE_REF="Done (Blue)" ID="ID_547843908" CREATED="1390480317273" MODIFIED="1390579983266"/>
<node TEXT="Differences:" ID="ID_1999957514" CREATED="1390480344405" MODIFIED="1390480352517"/>
<node TEXT="GH13 - Idea project: first iteration" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1802146546" CREATED="1390480725302" MODIFIED="1390480738933" LINK="https://github.com/flower-platform/flower-platform-4/issues/13">
<node TEXT="Redmine" ID="ID_414408123" CREATED="1390552689605" MODIFIED="1390552689605">
<node TEXT="RM8870 - GH13 - Idea project: first iteration" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_481079853" CREATED="1390552689605" MODIFIED="1390552689635" LINK="http://csp1/redmine/issues/8870"/>
<node TEXT="RM8871 - [Review] GH13 - Idea project: first iteration" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1339690434" CREATED="1390552689945" MODIFIED="1390552689955" LINK="http://csp1/redmine/issues/8871"/>
</node>
<node TEXT="Expected results" ID="ID_910899728" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT=".jetty, starts the server" ID="ID_937603805" CREATED="1390406429433" MODIFIED="1390406472239"/>
<node TEXT="editor" ID="ID_1730080755" CREATED="1390406475778" MODIFIED="1390406480562"/>
<node TEXT="menu action that starts editor" ID="ID_786778468" CREATED="1390406480808" MODIFIED="1390406808305"/>
<node TEXT="editor for .flower-diagram" ID="ID_504163183" CREATED="1390406808534" MODIFIED="1390406822653"/>
</node>
</node>
<node TEXT="GH16 - Preferences page" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1227884280" CREATED="1390563141166" MODIFIED="1390563162804" LINK="https://github.com/flower-platform/flower-platform-4/issues/16">
<node TEXT="Redmine" ID="ID_724648907" CREATED="1390919604230" MODIFIED="1390919604254">
<node TEXT="RM8911 - GH16 - Preferences page" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_791595867" CREATED="1390919604255" MODIFIED="1390919604277" LINK="http://csp1/redmine/issues/8911"/>
<node TEXT="RM8912 - [Review] GH16 - Preferences page" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_359663142" CREATED="1390919604600" MODIFIED="1390919604603" LINK="http://csp1/redmine/issues/8912"/>
</node>
</node>
<node TEXT="GH17 - Flower Platform Properties view" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1227764111" CREATED="1390563147222" MODIFIED="1390563166221" LINK="https://github.com/flower-platform/flower-platform-4/issues/17">
<node TEXT="Redmine" ID="ID_1566566258" CREATED="1390919614057" MODIFIED="1390919614058">
<node TEXT="RM8913 - GH17 - Flower Platform Properties view" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1715969204" CREATED="1390919614059" MODIFIED="1390919614064" LINK="http://csp1/redmine/issues/8913"/>
<node TEXT="RM8914 - [Review] GH17 - Flower Platform Properties view" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1526937505" CREATED="1390919617902" MODIFIED="1390919617904" LINK="http://csp1/redmine/issues/8914"/>
</node>
</node>
</node>
<node TEXT="flex_client.core" STYLE_REF="In progress (Yellow)" FOLDED="true" POSITION="right" ID="ID_292790199" CREATED="1394033875636" MODIFIED="1394034243769">
<node TEXT="About View" ID="ID_984740777" CREATED="1398766807367" MODIFIED="1398766812781">
<node TEXT="Display versions (client, server, api)" ID="ID_365291513" CREATED="1398766814534" MODIFIED="1398766830035"/>
<node TEXT="GH256 - About View" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1585593928" CREATED="1398766807367" MODIFIED="1398766880468" LINK="https://github.com/flower-platform/flower-platform-4/issues/256"/>
</node>
<node TEXT="Editor" STYLE_REF="In progress (Yellow)" FOLDED="true" ID="ID_1987869211" CREATED="1394033880786" MODIFIED="1394034245159">
<node TEXT="Editor Frontend Base" STYLE_REF="In progress (Yellow)" FOLDED="true" ID="ID_1998124581" CREATED="1394033908785" MODIFIED="1394034248439" LINK="#ID_1408702354">
<node TEXT="contains:" ID="ID_372835109" CREATED="1394033953235" MODIFIED="1394033964097"/>
<node TEXT="node registry" ID="ID_1459340806" CREATED="1394033964335" MODIFIED="1394033966377">
<node TEXT="processes updates from server" ID="ID_331690806" CREATED="1394034139397" MODIFIED="1394034155519"/>
</node>
<node TEXT="subscription mechanism" ID="ID_1809960431" CREATED="1394033966605" MODIFIED="1394033977797"/>
</node>
<node TEXT="Cross refs: resource node id &lt;-&gt; editors" STYLE_REF="In progress (Yellow)" ID="ID_178241386" CREATED="1394033984705" MODIFIED="1394034248439" LINK="#ID_1261755385"/>
<node TEXT="Timer for requesting updates" STYLE_REF="In progress (Yellow)" FOLDED="true" ID="ID_330818403" CREATED="1394034062436" MODIFIED="1394034248449" LINK="#ID_1671079444">
<node TEXT="When a server timestamp is received =&gt; timer is reset" ID="ID_1584124950" CREATED="1394034083366" MODIFIED="1394034102488"/>
<node TEXT="When it expires, it pings the server =&gt; receives updates and server timestamp" ID="ID_781545216" CREATED="1394034102706" MODIFIED="1394034136029"/>
</node>
<node TEXT="During remote method invocation, enrich server message with info (in headers)" STYLE_REF="In progress (Yellow)" FOLDED="true" ID="ID_386968615" CREATED="1394034163247" MODIFIED="1394034248449" LINK="#ID_1539197998">
<node TEXT="list of resource nodes" ID="ID_1910640289" CREATED="1394034194997" MODIFIED="1394034204609"/>
<node TEXT="server timestamp of last request" ID="ID_1530527135" CREATED="1394034204877" MODIFIED="1394034212799"/>
</node>
<node TEXT="Offline &quot;fail gracefully&quot; cases" STYLE_REF="New (Red)" FOLDED="true" ID="ID_1957604092" CREATED="1394115892074" MODIFIED="1394116390907" LINK="#ID_1424110088">
<node TEXT="All remote invocation methods need to wait for the server response. Only on succes, they should &quot;finish&quot; their job (e.g. close a popup (properties editor), removing IED, etc)" ID="ID_942140088" CREATED="1394115925494" MODIFIED="1394116112736"/>
<node TEXT="When a response for updates requests says that" ID="ID_1377384063" CREATED="1394116114854" MODIFIED="1394116218157">
<node TEXT="a resource doesn&apos;t exist any longer =&gt; find corresponding nodes (in various editors) and collapse. If was root =&gt; close editor. Of course: remove from map." ID="ID_1616562747" CREATED="1394116218157" MODIFIED="1395413967557"/>
<node TEXT="null for a resource (i.e. server doesn&apos;t remembers) =&gt; perform refresh for the resource" ID="ID_28503512" CREATED="1394116224265" MODIFIED="1394116258207"/>
</node>
<node TEXT="When there is no connection to server" ID="ID_1603348352" CREATED="1395414652537" MODIFIED="1395414658159">
<node TEXT="&quot;reconnecting&quot; spinner appears; reconnection timer is started (default 60s)" ID="ID_1601181339" CREATED="1395414660878" MODIFIED="1395414850563"/>
<node TEXT="The connection with the server has been lost. Last successful communication was at 2014-12-13 18:45:45 (local) / ... (server).&#xa;&#xa;Automatically trying to reconnect each x sec." ID="ID_1237405669" CREATED="1395414678113" MODIFIED="1395414790665"/>
<node TEXT="Cancel" ID="ID_87010201" CREATED="1395414791373" MODIFIED="1395414796278">
<node TEXT="on press: disables the timer" ID="ID_1557159123" CREATED="1395414797336" MODIFIED="1395414804970"/>
</node>
<node TEXT="Reconnect" ID="ID_831281677" CREATED="1395414806178" MODIFIED="1395414815014">
<node TEXT="on press: reconnect, and start reconnection timer" ID="ID_19249766" CREATED="1395414816162" MODIFIED="1395414835089"/>
</node>
<node TEXT="on reconnection timer expired =&gt; stop timer (i.e. stop retrying automatically; update message)" ID="ID_46807270" CREATED="1395414858353" MODIFIED="1395414881082"/>
</node>
<node TEXT="GH107 - Offline &quot;fail gracefully&quot; cases" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_182815998" CREATED="1394115892074" MODIFIED="1394116387377" LINK="https://github.com/flower-platform/flower-platform-4/issues/107">
<node TEXT="Redmine" ID="ID_1359714926" CREATED="1395415278945" MODIFIED="1395415278945">
<node TEXT="RM9185 - GH107 - Offline &quot;fail gracefully&quot; cases" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_3657820" CREATED="1395415278945" MODIFIED="1395415278965" LINK="http://csp1/redmine/issues/9185"/>
<node TEXT="RM9186 - [Review] GH107 - Offline &quot;fail gracefully&quot; cases" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_607045595" CREATED="1395415279485" MODIFIED="1395415279485" LINK="http://csp1/redmine/issues/9186"/>
</node>
<node TEXT="Construction site" ID="ID_991828837" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="add in wiki; don&apos;t publish" ID="ID_1554725768" CREATED="1395413682314" MODIFIED="1395413690927"/>
<node TEXT="As vrea si in UI, sa vad starea lui upd requested. Eventual un buton sa fortez. Poate si o setare pentru timp? Pe undeva prin meniu?" ID="ID_1346621306" CREATED="1394116302245" MODIFIED="1394116330247"/>
</node>
<node TEXT="Expected results#" ID="ID_1200776355" CREATED="1389360413465" MODIFIED="1394116368287">
<icon BUILTIN="revision"/>
<node TEXT="Offline &quot;fail gracefully&quot; cases" ID="ID_1424110088" CREATED="1394116378927" MODIFIED="1394116378947" LINK="#ID_1957604092"/>
</node>
</node>
</node>
<node TEXT="Dirty, save, reload, etc" STYLE_REF="Done (Blue)" FOLDED="true" ID="ID_1233269476" CREATED="1394116665483" MODIFIED="1396023707119" LINK="#ID_507863337">
<node TEXT="GH109 - Dirty, save, reload" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1368384999" CREATED="1394116665483" MODIFIED="1394116673935" LINK="https://github.com/flower-platform/flower-platform-4/issues/109">
<node TEXT="Construction site" ID="ID_1393699962" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="De reactivat si logica de JS, si dialogul de save multiplu, din FP2/3" ID="ID_360675158" CREATED="1394116701954" MODIFIED="1394116715785"/>
<node TEXT="de scris spec pe aici, si prin partea de server" ID="ID_248968850" CREATED="1394116725343" MODIFIED="1394116772045"/>
<node TEXT="Luat din FP3: Improvements &amp; Limitations" STYLE_REF="Features node (Blue cloud)" ID="ID_1166487005" CREATED="1355145852493" MODIFIED="1395069975157">
<font BOLD="true"/>
<node TEXT="Pentru curatenie" STYLE_REF="Planned (Orange)" FOLDED="true" ID="ID_1473749592" CREATED="1355146459727" MODIFIED="1355148594340">
<node TEXT="De scanat dupa CS/STFL" ID="ID_1982911997" CREATED="1355146469389" MODIFIED="1355146476971"/>
<node TEXT="de sters directoarele cu src comise inca pe SVN: java, as" ID="ID_1081399961" CREATED="1355146477549" MODIFIED="1355146493081"/>
<node TEXT="rename/move" ID="ID_1110471876" CREATED="1350370916267" MODIFIED="1350370922157">
<node TEXT="de mutat WebViewer" ID="ID_1833841054" CREATED="1349885113654" MODIFIED="1349885118849"/>
<node TEXT="facut uniforme numele de pachet, as/java" ID="ID_1492154448" CREATED="1349940704030" MODIFIED="1349940720818"/>
<node TEXT="EditorDescriptorNew" ID="ID_1729138363" CREATED="1350371014454" MODIFIED="1350371021818"/>
<node TEXT="Save New" ID="ID_118445215" CREATED="1350371022059" MODIFIED="1350371024431"/>
</node>
<node TEXT="de uniformizat numele pachetelor. acum avem .editor, .editorsupport + ca nu pare la fel in java si as" ID="ID_1635711669" CREATED="1355146500525" MODIFIED="1355146517754"/>
<node TEXT="pentru curatenie: de unificat ES cu GlobalEOM (as)" ID="ID_1072478797" CREATED="1354708404536" MODIFIED="1354708420091"/>
<node TEXT="de facut si modif de la STFL" ID="ID_1190106411" CREATED="1355149445409" MODIFIED="1355149454195"/>
</node>
<node TEXT="Probleme vazute&#xa;Posibil ca unele sa nu mai fie de actualitate; insa majoritatea inca sunt" STYLE_REF="Planned (Orange)" FOLDED="true" ID="ID_948334427" CREATED="1355147103853" MODIFIED="1355148594348">
<node TEXT="Save all face un lock la diagrama" LOCALIZED_STYLE_REF="default" FOLDED="true" ID="ID_418192379" CREATED="1349989134465" MODIFIED="1355147084265">
<node TEXT="Tot din familia asta: meniul contextual are ac. pb; cred ca atunci cand trec pe la lock sync sau o alta setare on/off" ID="ID_1814923136" CREATED="1350399882188" MODIFIED="1350399910139"/>
</node>
<node TEXT="Grupul AS e inaintea celui de Java, in toolbar" LOCALIZED_STYLE_REF="default" ID="ID_1244121518" CREATED="1349853854832" MODIFIED="1355147084267"/>
<node TEXT="Am impresia ca nu mai sunt folos" LOCALIZED_STYLE_REF="default" FOLDED="true" ID="ID_989240005" CREATED="1350303963103" MODIFIED="1355147084269">
<node TEXT="/com.crispico.flower.mp.web.app/flex_src/com/crispico/flower/mp/web/texteditor/EditorPane.as" ID="ID_1353285680" CREATED="1350303971933" MODIFIED="1350303972792"/>
<node TEXT="EditorViewer.as" ID="ID_691256591" CREATED="1350303986172" MODIFIED="1350303986173" LINK="../../../java_work/eclipse_workspace_fmp/com.crispico.flower.mp.web.app/flex_src/com/crispico/flower/mp/web/texteditor/EditorViewer.as"/>
<node TEXT="si referinte de prin ele (temp.*)" ID="ID_977388855" CREATED="1350303996799" MODIFIED="1350304003831"/>
</node>
<node TEXT="logica de keystroke aggr" FOLDED="true" ID="ID_1735390750" CREATED="1350306090157" MODIFIED="1355147940031">
<icon BUILTIN="messagebox_warning"/>
<node TEXT="TextEditorFrontend.mxml" ID="ID_1973329475" CREATED="1350306096024" MODIFIED="1350306096025" LINK="../../../java_work/eclipse_workspace_fmp/com.crispico.flower.mp.web.app/flex_src/com/crispico/flower/mp/web/texteditor/TextEditorFrontend.mxml"/>
<node TEXT="InitializeNewClientCommand.java" ID="ID_181468598" CREATED="1350307264246" MODIFIED="1350307264250" LINK="../../../java_work/eclipse_workspace_fmp/com.crispico.flower.mp.web/src/com/crispico/flower/mp/web/communication/command/client/InitializeNewClientCommand.java"/>
<node TEXT="ar trebui ca daca nu are nimic in buffer sa trimita imediat ceva caractere. Ca sa avem un efect frumos de modificare" ID="ID_131566556" CREATED="1349460836417" MODIFIED="1349460861828"/>
</node>
<node TEXT="Pb resubscribe legata de ordinea reconectarilor" FOLDED="true" ID="ID_546147708" CREATED="1349872514958" MODIFIED="1349872525775">
<node TEXT="[14:32:43] Mariana Gheorghe: Cristi, la reconectare client, ar fi un caz in care e importanta ordinea in care se face subscribe la STFL services: ER ar trebui inainte de doc si props. Motiv: avem o diagrama deschisa, fac switch user, daca se face subscribe mai intai la doc, o sa avem eroare pe server pt ca nu gaseste diagrama (clientul fusese unsubscribed de la diagrama, dar nu a apucat inca sa faca subscribe din nou)" ID="ID_1016451919" CREATED="1349872526525" MODIFIED="1349872526525"/>
<node TEXT="http://csp1/redmine/issues/7056" ID="ID_1937786126" CREATED="1349872537364" MODIFIED="1349872537364" LINK="http://csp1/redmine/issues/7056"/>
<node TEXT="Probabil tr. notiunea de prioritate" ID="ID_1020330910" CREATED="1349872539002" MODIFIED="1349872543995"/>
<node TEXT="La reconnect, da eroare (log) doc si props pentru ca nu gaseste ER" FOLDED="true" ID="ID_1751010946" CREATED="1354897197142" MODIFIED="1354897223472">
<node TEXT="Pentru ca ea se incarca un pic dupa. Ar trebui sa existe o prioritate mai mare de incarcare pentru ESC, si asta ar disparea" ID="ID_1024731868" CREATED="1354897224447" MODIFIED="1354897270614"/>
<node TEXT="18:19:15.891 [http-bio-8080-exec-10] ERROR c.c.f.m.w.m.WebIdRegistryProvider - Invalid client selection, diagramId provided but could not obtain DiagramEditableResource. DiagramId = 744 selectionIds = [] []" ID="ID_185117256" CREATED="1354897272600" MODIFIED="1354897275303"/>
</node>
</node>
<node TEXT="la inchidere editor, de vazut de cine mai e legat toolbar; are sens o stergere?" ID="ID_584661525" CREATED="1349848746388" MODIFIED="1349848760738"/>
<node TEXT="WebTransferAdapterLogicProvider.sendUpdateCommand()" FOLDED="true" ID="ID_1923786743" CREATED="1348495806070" MODIFIED="1348495815282">
<node TEXT="ar trebui ca aceasta logica sa existe in cadrul lui Stateful...; pentru a avea si lock?" ID="ID_1811825467" CREATED="1348495816475" MODIFIED="1348502660906"/>
</node>
<node TEXT="Problema agregare" FOLDED="true" ID="ID_812392292" CREATED="1348513599418" MODIFIED="1348513604109">
<node TEXT="Scriu ceva" ID="ID_1665662105" CREATED="1348513605149" MODIFIED="1348513612199"/>
<node TEXT="Apas unlock, si se deblocheaza" ID="ID_253380362" CREATED="1348513612447" MODIFIED="1348513619449"/>
<node TEXT="Apoi se blocheaza singur din nou: probabil pentru ca se transmite un calup netrimis de taste" ID="ID_676556383" CREATED="1348513619681" MODIFIED="1348513639378"/>
</node>
<node TEXT="De testat si inchidere editor (si alte operatiuni) cat timp datele nu au sosit" ID="ID_896567204" CREATED="1348725660849" MODIFIED="1348725679151"/>
<node TEXT="De testat si ce se intampla cand erPath nu (mai) indica catre ceva valid" ID="ID_225283187" CREATED="1349706893983" MODIFIED="1349706923983"/>
<node TEXT="Ce facem cu cei care au nevoie sa deschida modelul? E.g. documentation la un disconnect" ID="ID_255848651" CREATED="1348832272194" MODIFIED="1348832288093"/>
<node TEXT="De combinat codul de print, intre EdiotrStatefulService, si Download/UploadSC" ID="ID_981586219" CREATED="1349272683333" MODIFIED="1349272698182"/>
<node TEXT="ModelEditorStflSvc face apel la o serie de functii din ModelTreeSubService. Ar cam trebui uniformizate" ID="ID_263371807" CREATED="1349704903103" MODIFIED="1349704936448"/>
<node TEXT="Sistemul de acces concurential intre slave ER si master ER. Adica namedlock pool sa acopere si model + diagrama" FOLDED="true" ID="ID_1343043972" CREATED="1350295108601" MODIFIED="1355148238149">
<node TEXT="1" OBJECT="java.lang.Long|1" FOLDED="true" ID="ID_1484391386" CREATED="1350295912214" MODIFIED="1350295912856">
<node TEXT="Cred ca trebuie sa facem lock() pe calea master in momentul in care se fac operatiuni pe slave ER (conect, sendupdate, unsubscribe)" ID="ID_1024506490" CREATED="1350295127236" MODIFIED="1350295188906"/>
<node TEXT="Relatia dintre MESS si DESS de fapt e ca relatia dintre MESS si doc/props. Trebuie sa puna la dispozitie o metoda gen update(Runnable), si eu sa-mi fac operatiunea in acest runnable" ID="ID_115209163" CREATED="1350295189729" MODIFIED="1350295314764"/>
<node TEXT="Va trebui modificat si props/doc sa faca asta" ID="ID_576572613" CREATED="1350295317375" MODIFIED="1350295326356"/>
</node>
<node TEXT="2" OBJECT="java.lang.Long|2" FOLDED="true" ID="ID_1344275071" CREATED="1350295909951" MODIFIED="1350295911298">
<node TEXT="UnsubscribeInternal apeleaza slaveER.getERCByCCTrheadSafe(). Totusi, asta nu cred ca ma impiedica ca fix in aceasta perioada sa se faca o stergere? Sau am fi totusi acoperiti, caci apoi apelam de fapt unsubScribeForce, care apeleaza unsub, care face lock pe namedLockPool?" ID="ID_650229722" CREATED="1350295921761" MODIFIED="1350295995581"/>
</node>
<node TEXT="Daca orice op diagrama trece prin master/model, DESS.attempt... nu mai tr. sa trimita ER daca e slave; acum daca e slave, trimite pe master" ID="ID_375675774" CREATED="1350391107919" MODIFIED="1350391205861"/>
</node>
<node TEXT="iconite fisiere AS si MXML. Poate gasim pe la flash develop ceva. Sa nu le luam pe cele Adobe, caci au copyright ceva probabil" ID="ID_1434646515" CREATED="1350839453162" MODIFIED="1350839481699"/>
<node TEXT="vad ca exista diagrama trece in modul lock si la updatarea fig colab. De fapt, doar la move de figuri colab." ID="ID_1113238013" CREATED="1347431040377" MODIFIED="1347431098261"/>
</node>
<node TEXT="Feature Improvements" LOCALIZED_STYLE_REF="default" FOLDED="true" ID="ID_1716591409" CREATED="1340709324414" MODIFIED="1355149211640">
<node TEXT="Logica de versionare a mesajelor.&#xa;Initial credeam ca e necesar un astfel de sistem. Dar din practica, ac. lucru pare sa nu fie necesar. Se pare ca mai mereu mesajele ajung toate, si in ordinea dorita. Nu am vazut pana acum mesaje nelivrate sau ajunse intr-o ordine aiurea." FOLDED="true" ID="ID_1294393718" CREATED="1340709351834" MODIFIED="1355145793177">
<node TEXT="Scenariu pentru Concurenta" FOLDED="true" ID="ID_250702406" CREATED="1337873720081" MODIFIED="1340709540043">
<node TEXT="Clientul A si B, la versiunea 300. Cazul text editor&#xa;A si B au ultimaVersPrimita = 300" ID="ID_217737738" CREATED="1337873724155" MODIFIED="1337874549149"/>
<node TEXT="10:00:00: A modifica la versiunea 301, si intra in lock pentru 5 secunde.&#xa;A.ultVersPrimita = 300&#xa;A.ultVers = 301&#xa;Se face deci dispatch la 300-301 + lock&#xa;&#xa;B primeste mesajul la 10:00:02" ID="ID_1615705633" CREATED="1337873764210" MODIFIED="1337874588851"/>
<node TEXT="1" OBJECT="java.lang.Long|1" FOLDED="true" ID="ID_1107001006" CREATED="1337873761629" MODIFIED="1337873763377">
<node TEXT="10:00:03: nu se face dispatch; eventual aruncam chiar exceptie, caci controalele trebuiau blocate" ID="ID_1616243561" CREATED="1337873944812" MODIFIED="1337873982403"/>
</node>
<node TEXT="2" OBJECT="java.lang.Long|2" FOLDED="true" ID="ID_1505221241" CREATED="1337874040566" MODIFIED="1337874041656">
<node TEXT="10:00:01: B modifica si el&#xa;B.ultVersPrimita = 300&#xa;B.ultVers = 301&#xa;Pe server refuzam update (pt. ca lock) si tr. sa trimitem structura full&#xa;&#xa;In ac moment, in drum spre B, avem 2 mesaje: mesajul 300-301+lock si full update" ID="ID_1704718188" CREATED="1337874043125" MODIFIED="1337874608620"/>
<node TEXT="10:00:02 se primeste primul mesaj (300-301 + lock)&#xa;&#xa;B ar trebui sa refuze si sa puna ecranul de loading (modal la nivel de fereastra). Caci datele sunt pe drum.&#xa;Dar un full update este pe drum; deci el trebuie sa spuna ceva, ca serverul sa stie ca nu mai tr sa ii trimita nimic" ID="ID_885830092" CREATED="1337874220755" MODIFIED="1337875005368"/>
</node>
<node TEXT="Cat timp se asteapta full update = ecran loading" ID="ID_444558371" CREATED="1337875037708" MODIFIED="1337875119471"/>
</node>
</node>
<node TEXT="Imbunatatiri RES_CHANGED" FOLDED="true" ID="ID_4473817" CREATED="1340715615972" MODIFIED="1355145830389">
<node TEXT="Cand se redenumeste un fisier (sau diagrama, etc), sa incercam sa modificam si editorInput, fara a inchide editorul" ID="ID_556464186" CREATED="1340715701799" MODIFIED="1340715739539"/>
<node TEXT="Stergerea unui fisier de pe HDD" ID="ID_198363033" CREATED="1340715740059" MODIFIED="1340715749855"/>
<node TEXT="Blocarea accesului (tocmai s-au scos drepturile). In ultimele 2 cazuri, tr. sa dispara de pe client o ER ca urmare a comenzii serverului." ID="ID_930906031" CREATED="1340715750079" MODIFIED="1340715825285"/>
<node TEXT="Inchidere proiect" ID="ID_611627971" CREATED="1340724849565" MODIFIED="1340724852810"/>
</node>
<node TEXT="Fereastra cu Resursa curenta + zona de notificare in res status bar" FOLDED="true" ID="ID_10985513" CREATED="1340719096496" MODIFIED="1340719121445">
<node TEXT="chat" ID="ID_961614059" CREATED="1340719141167" MODIFIED="1340719142992"/>
<node TEXT="evenimente de gigi in, gogu out" ID="ID_1053331898" CREATED="1340719143216" MODIFIED="1340719149938"/>
<node TEXT="evenimente de save" ID="ID_1829442643" CREATED="1340719152033" MODIFIED="1340719155638"/>
<node TEXT="afisare evenimentul nou venit in zona de notificare + fade" ID="ID_153674786" CREATED="1340719167721" MODIFIED="1340719182348"/>
<node TEXT="Eventual si view-ul sa palpaie nitel" ID="ID_1576788232" CREATED="1340719182525" MODIFIED="1340719188780"/>
<node TEXT="Buton de &quot;open resources view&quot;" ID="ID_600783846" CREATED="1340719195818" MODIFIED="1340719204542"/>
</node>
<node TEXT="La deconectarea tuturor clientilor, poate e interes sa mai pastram o perioada mica de timp resursele?&#xa;Cateva propuneri:" FOLDED="true" ID="ID_872197677" CREATED="1340724888780" MODIFIED="1340724957964">
<node TEXT="Automatic save/discard." ID="ID_585167162" CREATED="1329487128886" MODIFIED="1329487144512"/>
<node TEXT="Idem, dar cu un timer." ID="ID_1577692555" CREATED="1329487145115" MODIFIED="1329487151116"/>
<node TEXT="Salvare (eventual cu un timer) sub un alt nume, si la urmatoarea deschidere (al vreunui client) afisam alerta." ID="ID_1225074698" CREATED="1329487152716" MODIFIED="1329487193187"/>
</node>
<node TEXT="Vizualizarea resurselor deschise de alti clienti&#xa;Poate vreun view nou, care sa semene cu Open Resources View? Folosibil de admini?" FOLDED="true" ID="ID_110379936" CREATED="1329560493764" MODIFIED="1340725015102">
<node TEXT="Va trebui sa avem si un view, care afiseaza lista elementelor deschise" FOLDED="true" ID="ID_1721461506" CREATED="1329560992358" MODIFIED="1329561039898">
<node TEXT="Inseamna ca (a) fie doar pentru acest view sau b) global in aplicatie) pe Flex sunt oglindite elementele deschise. Asta inseamna ca schimbari de statut, (open, close) ar trebui propagate pe clienti (in cazul a, doar la cei cu acest view deschis, in cazul b, tuturor clientilor." ID="ID_1950025626" CREATED="1329561074602" MODIFIED="1329561267380" BACKGROUND_COLOR="#ffffcc"/>
<node TEXT="Sau o varianta degradata, in care nu avem notificari, ci un buton refresh. La apasare, se reiau elementele de pe server." ID="ID_878962163" CREATED="1329561223634" MODIFIED="1329561267370" BACKGROUND_COLOR="#ffffcc"/>
</node>
</node>
<node TEXT="Lock" LOCALIZED_STYLE_REF="default" FOLDED="true" ID="ID_542812102" CREATED="1340819674608" MODIFIED="1355145948849">
<node TEXT="De periat logica de afisat minutele ramase la lock pentru a cazul (foarte probabil) in care exista o abatere intre client si server" LOCALIZED_STYLE_REF="default" FOLDED="true" ID="ID_1197093495" CREATED="1340108705723" MODIFIED="1340819711301">
<node TEXT="Daca client/server au time zone diferit, aplicatia functioneaza bine.&#xa;&#xa;De asemenea, e o problema legata de abaterea ceasurilor (client/server). Aveam o idee sa calculam o &quot;delta&quot;, insa tr. probabil introdus in ER un camp nou, care sa fie actualizat LA FIECARE transmisie a ER. Cel existent (lockUpdateTime) nu e prea bun caci ER poate sa se actualizeze intre timp, fara sa se act. si ac. camp. De asemenea, ce facem cu durata de transmisie a msg intre client si srv?&#xa;&#xa;Poate ar fi mai bine sa trimitem de pe  server direct delay-ul de expirarea? Insa si in czul asta, si in cel precedent, ar trebui sa facem o presupunere despre delay-ul de transmisie, eventual sa facem o interogare catre server si sa o cronometram." ID="ID_1664230602" CREATED="1340108719110" MODIFIED="1340711472173"/>
</node>
<node TEXT="Implementare si starea Read-Only, atunci cand se va introduce in sistem acest concept, SAU cand nu sunt drepturi de scriere." ID="ID_1329960400" CREATED="1340890309265" MODIFIED="1340890338402"/>
</node>
<node TEXT="Buton de refresh, ca sa testam full reload" LOCALIZED_STYLE_REF="default" ID="ID_1472490721" CREATED="1347904702449" MODIFIED="1355147084255"/>
<node TEXT="De implementat logica de pierdere de drepturi =&gt; se inchide fisierul" ID="ID_1083551778" CREATED="1355147185373" MODIFIED="1355147203519"/>
<node FOLDED="true" ID="ID_891364780" CREATED="1354710696552" MODIFIED="1355147900295"><richcontent TYPE="NODE">

<html>
  <head>
    
  </head>
  <body>
    <p>
      Daca avem nevoie de comportament suplimentar la <b>Open With</b>
    </p>
  </body>
</html>
</richcontent>
<node ID="ID_462743338" CREATED="1354710679489" MODIFIED="1354710893642"><richcontent TYPE="NODE">

<html>
  <head>
    
  </head>
  <body>
    <p>
      Posibil sa vrem sa avem un meniu Open With may mare, cam ca in eclipse (Default editor, other..., etc). Pentru asta, ar trebui sa facem o interfata, IEditorBase care are .getEditorName(); extinsa de ESS dar si de o clasa care nu are decat acel atribut. ES.defaultESS devine defaultEditor<b>s</b>&#xa0;ca tip ac. noua interfata, iar CTEntry.compatibleEditorStatefulServices devine compatibleEditors. Pe AS, vom crea EditorDesc pentru aceste editoare care nu au ESS si am rezolvat pb.
    </p>
  </body>
</html>
</richcontent>
</node>
</node>
</node>
<node TEXT="Limitari&#xa;(Unele rezolvabile, altele nu)" LOCALIZED_STYLE_REF="default" FOLDED="true" ID="ID_18162537" CREATED="1338532545852" MODIFIED="1355149205961">
<node TEXT="Functionale" FOLDED="true" ID="ID_1980576427" CREATED="1338540051145" MODIFIED="1338540055071">
<node TEXT="CTRL + S in IE nu functioneaza" FOLDED="true" ID="ID_1555135075" CREATED="1340023240933" MODIFIED="1340710402747">
<node TEXT="Flex nu il prinde si apare dialogul de  salvare fisier." ID="ID_397942958" CREATED="1340023246555" MODIFIED="1340023262440"/>
<node TEXT="In IE, putem prinde si impiedica evenimentul astfel:&#xa;&#xa;Obligatoriu punem sus:&#xa;&lt;!DOCTYPE html PUBLIC &quot;-//W3C//DTD HTML 4.01 Transitional//EN&quot; &quot;http://www.w3.org/TR/html4/loose.dtd&quot;&gt;&#xa;&#xa;apoi:&#xa;window.onkeydown = function(event) {&#xa;    alert(&quot;key&quot;);&#xa;if (event.which == 83 &amp;&amp; event.ctrlKey) {&#xa;     event.preventDefault();&#xa;     alert(&quot;haha&quot;);&#xa;    }&#xa;}&#xa;&#xa;INSA, sunt 2 pb: 1) cand focusul e pe app Flex, smecheria nu mai merge. 2) de la linia de sus, se afiseaza ciudat aplicatia, doar pe 30% din inaltime&#xa;&#xa;Am tot cautat si am impresia ca nu exista solutie. Buzzword de exemlu, propune ctrl+s pentru firefox, insa nimic pentru IE" ID="ID_600307415" CREATED="1340023262999" MODIFIED="1340023369214"/>
</node>
</node>
<node TEXT="Arhitectura" FOLDED="true" ID="ID_165269544" CREATED="1338540057150" MODIFIED="1338540060239">
<node TEXT="Remarca: am mers pe ideea ca nu putem avea mai multe nivele de master ER/slave ER. Daca vreuna din asta se modifica, tr. revizuit un pic codul." ID="ID_272984674" CREATED="1340283447314" MODIFIED="1355145724746"/>
<node TEXT="Sistemul de res. changed listener se bazeaza pe editorInput; daca asta o sa-si schimbe forma, trebuie gasita o solutie. Probabil, vor trebui indexate ER si dupa fisier." ID="ID_252937507" CREATED="1346417231671" MODIFIED="1346417303383"/>
</node>
</node>
<node TEXT="Imbunatatiri tehnice" FOLDED="true" ID="ID_949178043" CREATED="1355148411730" MODIFIED="1355148417965">
<node TEXT="Stocarea ER si in map dupa client?" FOLDED="true" ID="ID_429985020" CREATED="1348300902326" MODIFIED="1355148434268">
<icon BUILTIN="help"/>
<font BOLD="true"/>
<node TEXT="La unsubscribe, se itereaza pe tot map-ul. Daca descoperim ca e costisitor, ar fi o idee ca EditorStatefulService sa tina datele indexate si dupa WebCC; astfel la unsubscribe, am sti exact ce trebuie sters." ID="ID_767681110" CREATED="1348300906721" MODIFIED="1348300979667"/>
</node>
<node TEXT="Colaborare intre tree si EditorStatefulClient" FOLDED="true" ID="ID_320464965" CREATED="1349987325678" MODIFIED="1349987340096">
<node TEXT="Tree-ul sa tina o lista de ESC pe care le vede." ID="ID_301769534" CREATED="1349987379677" MODIFIED="1349987402910"/>
<node TEXT="Astfel, ProjExplTree isi stie propriul statut dirty. Acum foloseste setarea de dirty globala" ID="ID_1225644997" CREATED="1349453246380" MODIFIED="1349987415963"/>
<node TEXT="Ar implica ca atunci cand se deschide un model, sa vina o info in care sa ni se comunice si sClIdcorespunzator." ID="ID_96326312" CREATED="1349987419912" MODIFIED="1349987449297"/>
<node TEXT="Cred ca astfel am putea comanda si close node la inchidere ER, intr-un mod generic" ID="ID_1440951803" CREATED="1349987452747" MODIFIED="1349987485837"/>
<node TEXT="Asta ar permite si activarea butonului de Save pe un copil al unui model. Acum butonul de save nu e activat decat pe modelul propriu zis." ID="ID_1102443014" CREATED="1349987348154" MODIFIED="1349987378624"/>
<node TEXT="Caci sa nu uitam, ca conceptual, tree-ul modifica modelul, cand de fapt aceste modificari ar fi trebuit sa vina de la ModelEditorStatefulClient. Deci o colaborare are sens si conceptual vorbind." ID="ID_1990174666" CREATED="1349987567949" MODIFIED="1349987611000"/>
</node>
<node TEXT="Limitare: in momentul in cere porneste aplicatia si are deja editoare, au loc niste operatiuni suplimentare. Nu avem efecte negative, insa avem procesare client suplimentara" FOLDED="true" ID="ID_77009301" CREATED="1349860445342" MODIFIED="1349860860545">
<node TEXT="Se face subscribe de 2 ori" ID="ID_1978902912" CREATED="1349860498762" MODIFIED="1349860760464"/>
<node TEXT="primul: face cerere de pe server" ID="ID_647732409" CREATED="1349860762318" MODIFIED="1349860769450"/>
<node TEXT="al 2lea: inregistreaza listener pentru copy/local" ID="ID_252396110" CREATED="1349860770060" MODIFIED="1349860802203"/>
<node TEXT="vin datele, sunt e EFR, deci ambele se initializeaza" ID="ID_1872589164" CREATED="1349860802505" MODIFIED="1349860816309"/>
<node TEXT="se executa listenerul (i.e. abia acum se deseneaza ecranul): si face un reset al diagramei" ID="ID_82834378" CREATED="1349860816595" MODIFIED="1349860842854"/>
</node>
<node TEXT="Putem face mecanism general de subscribe comandat de server?&#xa;Cred ca sistem de unsubscribe comandat de server e mai simplu, dar trebuie si asta" FOLDED="true" ID="ID_842442186" CREATED="1349419794547" MODIFIED="1349424778453">
<node TEXT="Editorul de model are asa ceva" ID="ID_1574223982" CREATED="1350053936748" MODIFIED="1350053949578"/>
<node TEXT="Progres Monitor" ID="ID_539914724" CREATED="1350053952762" MODIFIED="1350053958592"/>
<node TEXT="Pentru ediotr (si in general) e problematic pentru ca subscribe deja presupune ca are un interlocutor, caruia vrea sa ii transmita date. Pentru modelEditor nu e grav, caci acolo nu se trimite nimic.&#xa;&#xa;Insa va trebui, atunci cand vom avea mod &quot;conferinta&quot;, adica cineva va comand deschiderea taburilor" ID="ID_254973102" CREATED="1349424854169" MODIFIED="1350054840265"/>
<node TEXT="Referitor la inchidere fortata: editorul suporta asta oarecum natural. Insa toate celelalte ar trebui sa  adauge explicit cod care sa se execute la unsub fortat (i.e. scoate acea fereastra, etc). + ca anumite lucruri poate nu e bine sa le scoatem: Progr mon, etc" ID="ID_877479412" CREATED="1350054844142" MODIFIED="1350054896504"/>
</node>
<node TEXT="Protectia de scriere la read-only a fost facuta in graba la indicatiile mele. Parca era ceva la care trebuia lucrat." ID="ID_1520669560" CREATED="1350480435179" MODIFIED="1350480722094"/>
</node>
</node>
</node>
<node TEXT="Expected results#" ID="ID_537404325" CREATED="1389360413465" MODIFIED="1394116368287">
<icon BUILTIN="revision"/>
<node TEXT="Dirty, save, reload" ID="ID_507863337" CREATED="1394116722325" MODIFIED="1394116722335" LINK="#ID_1233269476"/>
</node>
<node TEXT="Redmine" ID="ID_1239271782" CREATED="1395134074325" MODIFIED="1395134074335">
<node TEXT="RM9142 - GH109 - Dirty, save, reload" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_156150941" CREATED="1395134074335" MODIFIED="1395134074375" LINK="http://csp1/redmine/issues/9142"/>
<node TEXT="RM9143 - [Review] GH109 - Dirty, save, reload" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1610195107" CREATED="1395134074845" MODIFIED="1395134074855" LINK="http://csp1/redmine/issues/9143"/>
</node>
<node TEXT="Scenarios" STYLE_REF="Features node (Blue cloud)" FOLDED="true" ID="ID_517876311" CREATED="1355144059240" MODIFIED="1355144994368">
<node TEXT="Close" ID="ID_283259812" CREATED="1348243383737" MODIFIED="1348903867307">
<node TEXT="Inchidere editoare" ID="ID_485340105" CREATED="1348244408137" MODIFIED="1348244415923">
<node TEXT="Calculam lista de resourceNodes dirty" ID="ID_1559038697" CREATED="1348244442351" MODIFIED="1396249340405">
<node TEXT="Inlaturam resursele non dirty" LOCALIZED_STYLE_REF="default" ID="ID_98803706" CREATED="1348244606425" MODIFIED="1379343822180"/>
<node TEXT="Inlaturare duplicate" LOCALIZED_STYLE_REF="default" ID="ID_979188556" CREATED="1348244582491" MODIFIED="1379343822180"/>
</node>
<node TEXT="Afisam lista de mai sus." ID="ID_1273834362" CREATED="1348244643364" MODIFIED="1348244662430">
<node TEXT="Userul eventual poate debifa" LOCALIZED_STYLE_REF="default" ID="ID_1120590816" CREATED="1348244663039" MODIFIED="1379343822165"/>
<node TEXT="Lansam comenzile de salvare pentru fiecare element bifat" LOCALIZED_STYLE_REF="default" ID="ID_922230056" CREATED="1348244670372" MODIFIED="1379343822165"/>
<node TEXT="Caz part: daca avem o singura resursa, afisam in format &quot;dialog&quot;; altfel, format lista" LOCALIZED_STYLE_REF="default" ID="ID_1801360477" CREATED="1348244689895" MODIFIED="1379343822165"/>
</node>
<node TEXT="Dupa salvare (daca a fost cazul), luam fiecare editor si il inchidem" ID="ID_1019333406" CREATED="1348244730175" MODIFIED="1348244763723"/>
</node>
</node>
<node TEXT="Save All" FOLDED="true" ID="ID_1129198521" CREATED="1348853207826" MODIFIED="1348853210455">
<node TEXT="initial activata" ID="ID_413545875" CREATED="1348853246118" MODIFIED="1348853250789">
<node TEXT="ramane activata" ID="ID_1876126319" CREATED="1348853260039" MODIFIED="1348853264160">
<node TEXT="Plecare editor dirty, dar nu ultimul" LOCALIZED_STYLE_REF="default" ID="ID_1753721076" CREATED="1348853324931" MODIFIED="1379343822165"/>
<node TEXT="Salvare editoare dar nu pe toate" LOCALIZED_STYLE_REF="default" ID="ID_1284971449" CREATED="1348853401330" MODIFIED="1379343822165"/>
</node>
<node TEXT="devine dezactivata" ID="ID_1815265632" CREATED="1348853264417" MODIFIED="1348853271780">
<node TEXT="Plecare ultimul editor dirty" LOCALIZED_STYLE_REF="default" ID="ID_324057353" CREATED="1348853314925" MODIFIED="1379343822165"/>
<node TEXT="Salvare ultimul editor dirty" LOCALIZED_STYLE_REF="default" ID="ID_156382083" CREATED="1348853379742" MODIFIED="1379343822165"/>
<node TEXT="invocare save all" LOCALIZED_STYLE_REF="default" ID="ID_694283802" CREATED="1348853385446" MODIFIED="1379343822165"/>
</node>
</node>
<node TEXT="initial dezactivata" ID="ID_1589601070" CREATED="1348853251341" MODIFIED="1348853255065">
<node TEXT="devine activata" ID="ID_1765427475" CREATED="1348853278621" MODIFIED="1348853281759">
<node TEXT="Sosire editor dirty" LOCALIZED_STYLE_REF="default" ID="ID_1665042080" CREATED="1348853356913" MODIFIED="1379343822149"/>
<node TEXT="Un editor devine dirty" LOCALIZED_STYLE_REF="default" ID="ID_543590773" CREATED="1348853361369" MODIFIED="1379343822149"/>
</node>
<node TEXT="ramane dezactivata" ID="ID_1408427901" CREATED="1348853282112" MODIFIED="1348853289303">
<node TEXT="Plecare editor" LOCALIZED_STYLE_REF="default" ID="ID_319557870" CREATED="1348853340737" MODIFIED="1379343822149"/>
<node TEXT="Sosire editor non dirty" LOCALIZED_STYLE_REF="default" ID="ID_346982825" CREATED="1348853346334" MODIFIED="1379343822149"/>
</node>
</node>
</node>
<node TEXT="Save" FOLDED="true" ID="ID_572849306" CREATED="1349283118078" MODIFIED="1349283119584">
<node TEXT="Se activeaza cand" ID="ID_1378863360" CREATED="1349283139781" MODIFIED="1349283170562">
<node TEXT="Editorul curent devine dirty" LOCALIZED_STYLE_REF="default" ID="ID_969757279" CREATED="1349283171980" MODIFIED="1379343822149"/>
<node TEXT="Schmibam focus pe un editor dirty" LOCALIZED_STYLE_REF="default" ID="ID_1796506929" CREATED="1349283176635" MODIFIED="1379343822149"/>
<node TEXT="Inchidem un editor non-dirty si se activeaza unul dirty" LOCALIZED_STYLE_REF="default" ID="ID_805715351" CREATED="1349286785855" MODIFIED="1379343822149"/>
<node TEXT="View-ul curent isi schimba selectia, catre un element dirty" LOCALIZED_STYLE_REF="default" ID="ID_1957932344" CREATED="1349283231339" MODIFIED="1396249309165"/>
</node>
<node TEXT="Se dezactiveaza cand" ID="ID_395733291" CREATED="1349283206792" MODIFIED="1349283210990">
<node TEXT="Editorul curent devine not dirty" LOCALIZED_STYLE_REF="default" ID="ID_1857179339" CREATED="1349283214056" MODIFIED="1379343822149"/>
<node TEXT="Schimbam focus pe un editor non dirty" LOCALIZED_STYLE_REF="default" ID="ID_1253191461" CREATED="1349283220585" MODIFIED="1379343822134"/>
<node TEXT="Inchidem un editor dirty si se activeaza unul non-dirty" LOCALIZED_STYLE_REF="default" ID="ID_1114287844" CREATED="1349286785855" MODIFIED="1379343822134"/>
<node TEXT="View-ul curent isi schimba selectia, catre un element non dirty" LOCALIZED_STYLE_REF="default" ID="ID_1110826659" CREATED="1349283231339" MODIFIED="1396249319235"/>
</node>
</node>
</node>
</node>
<node TEXT="Scenarios" STYLE_REF="Features node (Blue cloud)" FOLDED="true" ID="ID_1686675868" CREATED="1355144059240" MODIFIED="1355144994368">
<node TEXT="Close" ID="ID_91415379" CREATED="1348243383737" MODIFIED="1348903867307">
<node TEXT="Inchidere editoare" ID="ID_1737188241" CREATED="1348244408137" MODIFIED="1348244415923">
<node TEXT="Calculam lista de EditableResource dirty" ID="ID_214499494" CREATED="1348244442351" MODIFIED="1348244474439">
<node TEXT="Inlaturam resursele non dirty" LOCALIZED_STYLE_REF="default" ID="ID_1542758409" CREATED="1348244606425" MODIFIED="1379343822180"/>
<node TEXT="Daca e o resursa slave, folosim master-ul sau." LOCALIZED_STYLE_REF="default" ID="ID_984067001" CREATED="1348244478294" MODIFIED="1379343822180"/>
<node TEXT="Inlaturare duplicate" LOCALIZED_STYLE_REF="default" ID="ID_901301609" CREATED="1348244582491" MODIFIED="1379343822180"/>
</node>
<node TEXT="Afisam lista de mai sus." FOLDED="true" ID="ID_1058439177" CREATED="1348244643364" MODIFIED="1348244662430">
<node TEXT="Userul eventual poate debifa" LOCALIZED_STYLE_REF="default" ID="ID_1736406805" CREATED="1348244663039" MODIFIED="1379343822165"/>
<node TEXT="Lansam comenzile de salvare pentru fiecare element bifat" LOCALIZED_STYLE_REF="default" ID="ID_966603797" CREATED="1348244670372" MODIFIED="1379343822165"/>
<node TEXT="Caz part: daca avem o singura resursa, afisam in format &quot;dialog&quot;; altfel, format lista" LOCALIZED_STYLE_REF="default" ID="ID_1839847624" CREATED="1348244689895" MODIFIED="1379343822165"/>
</node>
<node TEXT="Dupa salvare (daca a fost cazul), luam fiecare editor si il inchidem" FOLDED="true" ID="ID_324469704" CREATED="1348244730175" MODIFIED="1348244763723">
<node TEXT="Daca este ultimul editor, atunci se fa distruge si EditorStatefulClient + unsubscribe" LOCALIZED_STYLE_REF="default" ID="ID_749544592" CREATED="1348244764605" MODIFIED="1379343822165"/>
</node>
</node>
<node TEXT="Inchidere resurse (din Open Resources View)" FOLDED="true" ID="ID_824966573" CREATED="1348244923806" MODIFIED="1348244936449">
<node TEXT="idem ca mai sus; doar ca la ultima etapa, nu avem nici o lista de editoare. Trebuie deci sa spun EditorStatefulClient: sterge toate EFR + unsub; pentru master, el se va ocupa si pentru slave." LOCALIZED_STYLE_REF="default" ID="ID_1226785622" CREATED="1348244937090" MODIFIED="1379343822165"/>
</node>
</node>
<node TEXT="Save All" FOLDED="true" ID="ID_608237493" CREATED="1348853207826" MODIFIED="1348853210455">
<node TEXT="initial activata" FOLDED="true" ID="ID_1652291386" CREATED="1348853246118" MODIFIED="1348853250789">
<node TEXT="ramane activata" FOLDED="true" ID="ID_850553789" CREATED="1348853260039" MODIFIED="1348853264160">
<node TEXT="Plecare editor dirty, dar nu ultimul" LOCALIZED_STYLE_REF="default" ID="ID_69532390" CREATED="1348853324931" MODIFIED="1379343822165"/>
<node TEXT="Salvare editoare dar nu pe toate" LOCALIZED_STYLE_REF="default" ID="ID_617041227" CREATED="1348853401330" MODIFIED="1379343822165"/>
</node>
<node TEXT="devine dezactivata" FOLDED="true" ID="ID_518494433" CREATED="1348853264417" MODIFIED="1348853271780">
<node TEXT="Plecare ultimul editor dirty" LOCALIZED_STYLE_REF="default" ID="ID_627194621" CREATED="1348853314925" MODIFIED="1379343822165"/>
<node TEXT="Salvare ultimul editor dirty" LOCALIZED_STYLE_REF="default" ID="ID_1771931165" CREATED="1348853379742" MODIFIED="1379343822165"/>
<node TEXT="invocare save all" LOCALIZED_STYLE_REF="default" ID="ID_1228461660" CREATED="1348853385446" MODIFIED="1379343822165"/>
</node>
</node>
<node TEXT="initial dezactivata" FOLDED="true" ID="ID_923388778" CREATED="1348853251341" MODIFIED="1348853255065">
<node TEXT="devine activata" FOLDED="true" ID="ID_1100876317" CREATED="1348853278621" MODIFIED="1348853281759">
<node TEXT="Sosire editor dirty" LOCALIZED_STYLE_REF="default" ID="ID_721698835" CREATED="1348853356913" MODIFIED="1379343822149"/>
<node TEXT="Un editor devine dirty" LOCALIZED_STYLE_REF="default" ID="ID_585799331" CREATED="1348853361369" MODIFIED="1379343822149"/>
</node>
<node TEXT="ramane dezactivata" FOLDED="true" ID="ID_719364285" CREATED="1348853282112" MODIFIED="1348853289303">
<node TEXT="Plecare editor" LOCALIZED_STYLE_REF="default" ID="ID_471790593" CREATED="1348853340737" MODIFIED="1379343822149"/>
<node TEXT="Sosire editor non dirty" LOCALIZED_STYLE_REF="default" ID="ID_737729815" CREATED="1348853346334" MODIFIED="1379343822149"/>
</node>
</node>
</node>
<node TEXT="Save" FOLDED="true" ID="ID_213797330" CREATED="1349283118078" MODIFIED="1349283119584">
<node TEXT="Se activeaza cand" FOLDED="true" ID="ID_1785667935" CREATED="1349283139781" MODIFIED="1349283170562">
<node TEXT="Editorul curent devine dirty" LOCALIZED_STYLE_REF="default" ID="ID_1925233316" CREATED="1349283171980" MODIFIED="1379343822149"/>
<node TEXT="Schmibam focus pe un editor dirty" LOCALIZED_STYLE_REF="default" ID="ID_1229786287" CREATED="1349283176635" MODIFIED="1379343822149"/>
<node TEXT="Inchidem un editor non-dirty si se activeaza unul dirty" LOCALIZED_STYLE_REF="default" ID="ID_1652531209" CREATED="1349286785855" MODIFIED="1379343822149"/>
<node TEXT="View-ul curent (tree) isi schimba selectia, catre un element dirty" LOCALIZED_STYLE_REF="default" ID="ID_506270508" CREATED="1349283231339" MODIFIED="1379343822149"/>
</node>
<node TEXT="Se dezactiveaza cand" FOLDED="true" ID="ID_743086159" CREATED="1349283206792" MODIFIED="1349283210990">
<node TEXT="Editorul curent devine not dirty" LOCALIZED_STYLE_REF="default" ID="ID_527848540" CREATED="1349283214056" MODIFIED="1379343822149"/>
<node TEXT="Schimbam focus pe un editor non dirty" LOCALIZED_STYLE_REF="default" ID="ID_1910758605" CREATED="1349283220585" MODIFIED="1379343822134"/>
<node TEXT="Inchidem un editor dirty si se activeaza unul non-dirty" LOCALIZED_STYLE_REF="default" ID="ID_416442642" CREATED="1349286785855" MODIFIED="1379343822134"/>
<node TEXT="View-ul curent (tree) isi schimba selectia, catre un element non dirty" LOCALIZED_STYLE_REF="default" ID="ID_928664584" CREATED="1349283231339" MODIFIED="1379343822134"/>
</node>
</node>
<node TEXT="La reconectare" FOLDED="true" ID="ID_1824306769" CREATED="1349434473081" MODIFIED="1349434490219">
<node TEXT="Editoarele isi reactualizeaza continutul" LOCALIZED_STYLE_REF="default" ID="ID_1970390839" CREATED="1349434503171" MODIFIED="1379343822134"/>
<node TEXT="Editoarele isi reactualizeaza dirty + alte elemente de state (e.g. lock daca cineva tocmai editeaza)" LOCALIZED_STYLE_REF="default" ID="ID_1977412689" CREATED="1349434523872" MODIFIED="1379343822134">
<hook NAME="FirstGroupNode"/>
</node>
<node TEXT="Se actualizeaza statutul de dirty al modelelor deschise (vizibil in OpenResourcesView dar si in Project Explorer)" LOCALIZED_STYLE_REF="default" ID="ID_1051643503" CREATED="1349434549277" MODIFIED="1379343822118"/>
<node TEXT="ER-urile pe care nu mai pot sa le vad (nu mai exista sau nu mai am drepturi): dispar" LOCALIZED_STYLE_REF="default" ID="ID_80865162" CREATED="1349434634378" MODIFIED="1379343822118"/>
<node TEXT="Statutul actiunilor de save se actualizeaza corespunzator" ID="ID_1179399467" CREATED="1349434608957" MODIFIED="1349434631361">
<hook NAME="SummaryNode"/>
</node>
</node>
<node TEXT="Logout &amp; co" FOLDED="true" ID="ID_1191956218" CREATED="1352379738823" MODIFIED="1352379744928">
<node TEXT="User logs out =&gt; we invoke the save dialog. If OK =&gt; save &amp; logout. If cancel =&gt; logout is canceled" LOCALIZED_STYLE_REF="default" ID="ID_1912793823" CREATED="1352379746009" MODIFIED="1379343822118"/>
<node TEXT="Idem, but with switch dialog." LOCALIZED_STYLE_REF="default" ID="ID_306541883" CREATED="1352379878239" MODIFIED="1379343822118"/>
<node TEXT="User closes tab/browser window, and there are resources dirty =&gt;" LOCALIZED_STYLE_REF="default" FOLDED="true" ID="ID_1371327453" CREATED="1352379886029" MODIFIED="1352382787964">
<node TEXT="if user disconnected (e.g. after a deconnection from server) =&gt; do nothing" LOCALIZED_STYLE_REF="default" ID="ID_1373850759" CREATED="1352380010195" MODIFIED="1379343822118"/>
<node TEXT="otherwise: prevent closing through JavaScript, and invoke save dialog." LOCALIZED_STYLE_REF="default" ID="ID_1230671352" CREATED="1352380152521" MODIFIED="1379343822102"/>
</node>
</node>
<node TEXT="Open actions" FOLDED="true" ID="ID_1634324256" CREATED="1354705819332" MODIFIED="1354705823898">
<node TEXT="Multiple selection =&gt; only Open visible" LOCALIZED_STYLE_REF="default" ID="ID_1186981224" CREATED="1354776689416" MODIFIED="1379343822102"/>
<node TEXT="Single resource selected," FOLDED="true" ID="ID_979642280" CREATED="1354776706438" MODIFIED="1354776723090">
<node TEXT="And not open" FOLDED="true" ID="ID_1532085032" CREATED="1354776724508" MODIFIED="1354776732920">
<node TEXT="If only one compatible editor =&gt; Open With not visible" LOCALIZED_STYLE_REF="default" ID="ID_36673282" CREATED="1354776734440" MODIFIED="1379343822102"/>
<node TEXT="If multiple compatible editors =&gt; Open With visible" LOCALIZED_STYLE_REF="default" ID="ID_1208195730" CREATED="1354776755398" MODIFIED="1379343822102"/>
</node>
<node TEXT="And open =&gt;" FOLDED="true" ID="ID_1069826311" CREATED="1354776774165" MODIFIED="1354776780952">
<node TEXT="Open With visible (even if we have only 1 editor) and label is &quot;Open With (in New Editor)&quot;" LOCALIZED_STYLE_REF="default" ID="ID_1520010939" CREATED="1354776781840" MODIFIED="1379343822102"/>
</node>
</node>
<node TEXT="Click open," FOLDED="true" ID="ID_721878474" CREATED="1354777129157" MODIFIED="1354777138697">
<node TEXT="and resource not open =&gt; resource is opened" LOCALIZED_STYLE_REF="default" ID="ID_1015913187" CREATED="1354777139878" MODIFIED="1379343822087"/>
<node TEXT="and resource is open =&gt; focus on the corresponding tab" LOCALIZED_STYLE_REF="default" ID="ID_378722193" CREATED="1354777153598" MODIFIED="1379343822071"/>
</node>
<node TEXT="Click Open With =&gt; opens a new editor (even if one exists)" LOCALIZED_STYLE_REF="default" ID="ID_1282102682" CREATED="1354777213812" MODIFIED="1379343822087"/>
</node>
<node TEXT="Model" FOLDED="true" ID="ID_341313928" CREATED="1349446932962" MODIFIED="1349446935056">
<node TEXT="Deschidere model din tree, prin apasare pe &quot;+&quot; la nodul din tree. =&gt;apare nodul in open res view" LOCALIZED_STYLE_REF="default" ID="ID_823697388" CREATED="1349446936625" MODIFIED="1379343822071"/>
<node TEXT="Alt user deschide model din tree =&gt; se actualizeaza nodul cu numarul de clienti" LOCALIZED_STYLE_REF="default" ID="ID_456520404" CREATED="1349446991383" MODIFIED="1379343822071"/>
<node TEXT="Operatiunile care sunt facute pe model implica modificarea starii de dirty" LOCALIZED_STYLE_REF="default" ID="ID_618045650" CREATED="1349447327260" MODIFIED="1379343822056"/>
</node>
<node TEXT="Tree Project Explorer" FOLDED="true" ID="ID_276718226" CREATED="1349451690958" MODIFIED="1349451697613">
<node TEXT="Schimbare de selectie pe/de pe o resursa dirty =&gt; activeaza/dezactiveaza actiunea de save" LOCALIZED_STYLE_REF="default" ID="ID_741232609" CREATED="1349455030176" MODIFIED="1379343822056"/>
<node TEXT="Afisare * in dreptul resurselor dirty" LOCALIZED_STYLE_REF="default" ID="ID_1689141464" CREATED="1349461612759" MODIFIED="1379343822040"/>
<node TEXT="Master/Slave" FOLDED="true" ID="ID_1565348575" CREATED="1349883533996" MODIFIED="1349883541480">
<node TEXT="Master devine dirty/clean (in urma actiunii mele sau a altcuiva) =&gt; slave se modif" LOCALIZED_STYLE_REF="default" ID="ID_508195970" CREATED="1349883543033" MODIFIED="1379343788625"/>
<node TEXT="Viceversa, i.e. slave devine dirty/clean =&gt; master se modif; acest slave poate eu nu il am in browser" LOCALIZED_STYLE_REF="default" ID="ID_738007063" CREATED="1349883567703" MODIFIED="1379343788640"/>
<node TEXT="Inchidere grup de editoare care contin slave dirty cu ac. master =&gt; e afisat o singura data master in lista" LOCALIZED_STYLE_REF="default" ID="ID_1281768734" CREATED="1349988006379" MODIFIED="1379343788640"/>
<node TEXT="La Save All, cu master si slave-uri (cu ac. master) dirty =&gt; se transmite o singura comanda, pentru master" LOCALIZED_STYLE_REF="default" ID="ID_160277210" CREATED="1349988066411" MODIFIED="1379343788640"/>
<node TEXT="La Save pe un master (din meniu sau buton) =&gt; se transmite pentru master" LOCALIZED_STYLE_REF="default" ID="ID_1540047247" CREATED="1349988138337" MODIFIED="1379343788656"/>
<node TEXT="Unsubscribe (din proj expl) de la o master =&gt; unsub si slave" LOCALIZED_STYLE_REF="default" ID="ID_1497908251" CREATED="1349982775932" MODIFIED="1379343788656"/>
</node>
</node>
<node TEXT="Open Resources View" FOLDED="true" ID="ID_1229655139" CREATED="1349984788208" MODIFIED="1349984793153">
<node TEXT="Deschiderea/inchiderea unei slave =&gt; recalcularea etichetei" LOCALIZED_STYLE_REF="default" ID="ID_70046046" CREATED="1349984798166" MODIFIED="1379343788625">
<icon BUILTIN="button_ok"/>
</node>
</node>
</node>
</node>
<node TEXT="Link Handler" STYLE_REF="New (Red)" ID="ID_866819647" CREATED="1394808012272" MODIFIED="1394808016184">
<node TEXT="GH129 - LinkHandler: When opening multiple resources: take into account the selected index" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_381772505" CREATED="1394793441622" MODIFIED="1394808049927" LINK="https://github.com/flower-platform/flower-platform-4/issues/129"/>
</node>
<node TEXT="Content type registry" STYLE_REF="New (Red)" ID="ID_1579198486" CREATED="1395137790508" MODIFIED="1395137840637">
<node TEXT="GH142 - Content type registry" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_95529721" CREATED="1395137790508" MODIFIED="1395137807527" LINK="https://github.com/flower-platform/flower-platform-4/issues/142">
<node TEXT="injectare content type (e.g. pentru file). Exista un map in client, de la content la tip de editor" ID="ID_384021305" CREATED="1392910601764" MODIFIED="1392910622185"/>
<node TEXT="de facut editor nou de text (gol)" ID="ID_37411328" CREATED="1395320518917" MODIFIED="1395320541986"/>
<node TEXT="toate fisierele au ct = text" ID="ID_335168219" CREATED="1395320542310" MODIFIED="1395320570243"/>
<node TEXT="fisierele mm au ct = mindmap" ID="ID_3270384" CREATED="1395320571209" MODIFIED="1395320582666"/>
<node TEXT="logica de open din flex: dc !ex ct =&gt; ct = mindmap" ID="ID_27659881" CREATED="1395320600259" MODIFIED="1395320625382">
<node TEXT="editorul de mm se declara default" ID="ID_1211255151" CREATED="1395320632617" MODIFIED="1395320646787"/>
</node>
<node TEXT="fisierele mm au &quot;hideRootNode = true&quot;" ID="ID_14291351" CREATED="1395320711530" MODIFIED="1395320722152"/>
<node TEXT="Redmine" ID="ID_1064934403" CREATED="1395158103085" MODIFIED="1395158103088">
<node TEXT="RM9158 - GH142 - Content type registry" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1996115714" CREATED="1395158103090" MODIFIED="1395158103100" LINK="http://csp1/redmine/issues/9158"/>
<node TEXT="RM9159 - [Review] GH142 - Content type registry" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_559633656" CREATED="1395158103675" MODIFIED="1395158103682" LINK="http://csp1/redmine/issues/9159"/>
</node>
</node>
</node>
</node>
<node TEXT="Client/server version check" STYLE_REF="Done (Blue)" ID="ID_123937905" CREATED="1395232986065" MODIFIED="1397039749365">
<node TEXT="Anti cache" ID="ID_1500308368" CREATED="1397653900095" MODIFIED="1397654077785">
<node TEXT="Append &quot;version=....&quot; to resources:" ID="ID_458220462" CREATED="1397654079916" MODIFIED="1397654102979"/>
<node TEXT="in main.jsp (actually that&apos;s why this file is a JSP)" ID="ID_1482540487" CREATED="1397654414607" MODIFIED="1397654429294">
<node TEXT="main swf file" ID="ID_186378022" CREATED="1397654103247" MODIFIED="1397654113429"/>
<node TEXT="flexHostApp.js" ID="ID_371122926" CREATED="1397654113807" MODIFIED="1397654129825"/>
</node>
<node TEXT="FlexPluginManager.appendVersionToUrl" STYLE_REF="Code" ID="ID_757683768" CREATED="1397654812163" MODIFIED="1397654841800">
<node TEXT="if not null, it is appended to the url of the actual plugins" ID="ID_652716404" CREATED="1397654845520" MODIFIED="1397654857808"/>
</node>
</node>
<node TEXT="Plugin versions check" ID="ID_75769974" CREATED="1397655207710" MODIFIED="1397655234603">
<node TEXT="The build script, writes in flex (clientAppVersion.inc)." ID="ID_270731271" CREATED="1397655236191" MODIFIED="1397655627443"/>
<node TEXT="All plugins will do: this.clientAppVersion = inc; this field belongs to AbstractFlowerFlexPlugin" ID="ID_889378140" CREATED="1397655312050" MODIFIED="1397655461734"/>
<node TEXT="AbstractFlowerFlexPlugin.checkClientAppVersion" STYLE_REF="Code" ID="ID_1130276738" CREATED="1397655462152" MODIFIED="1397655477490">
<node TEXT="called from preStart" ID="ID_1736764760" CREATED="1397655481670" MODIFIED="1397655485464"/>
<node TEXT="verifies with CorePlugin.clientAppVersion" ID="ID_494196782" CREATED="1397655501708" MODIFIED="1397655519496"/>
</node>
</node>
<node TEXT="API version check" ID="ID_1076784727" CREATED="1397654893846" MODIFIED="1397654925936">
<node TEXT="The build script, writes the serverAppVersion and serverAPIVersion in java (CoreConstants) and in flex (serverAPIVersion.inc)" ID="ID_611630266" CREATED="1397655236191" MODIFIED="1397655794341"/>
<node TEXT="All plugins will do: this.serverAPIVersion = inc; this field belongs to AbstractFlowerFlexPlugin" ID="ID_574458922" CREATED="1397655312050" MODIFIED="1397655826224"/>
<node TEXT="CoreService.getVersions() [server, api]" ID="ID_1311913172" CREATED="1397655663324" MODIFIED="1397655683024"/>
<node TEXT="called at the beginning by Flex, and saved in CorePlugin" ID="ID_1952950552" CREATED="1397655685114" MODIFIED="1397655765489"/>
<node TEXT="AbstractFlowerFlexPlugin.checkAPIVersion()" STYLE_REF="Code" ID="ID_1077225311" CREATED="1397655706602" MODIFIED="1397655727682">
<node TEXT="called from preStart" ID="ID_1020259934" CREATED="1397655481670" MODIFIED="1397655485464"/>
<node TEXT="verifies with CorePlugin.serverAPIVersion" ID="ID_1013097759" CREATED="1397655501708" MODIFIED="1397655845501"/>
</node>
</node>
<node TEXT="GH155 - Check version compatibility on client startup" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_644928795" CREATED="1394794004192" MODIFIED="1395233030613" LINK="https://github.com/flower-platform/flower-platform-4/issues/155">
<node TEXT="Redmine" ID="ID_829297879" CREATED="1396250614630" MODIFIED="1396250614650">
<node TEXT="RM9236 - GH155 - Check version compatibility on client startup" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_490823046" CREATED="1396250614660" MODIFIED="1396250614680" LINK="http://csp1/redmine/issues/9236"/>
<node TEXT="RM9237 - [Review] GH155 - Check version compatibility on client startup" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1413898886" CREATED="1396250615250" MODIFIED="1396250615260" LINK="http://csp1/redmine/issues/9237"/>
</node>
</node>
<node TEXT="De afisat versiunea: si in mobil si in web" ID="ID_299506102" CREATED="1397652633183" MODIFIED="1397652644021"/>
</node>
<node TEXT="Parameters (injected by server)" STYLE_REF="New (Red)" ID="ID_423095702" CREATED="1395233201754" MODIFIED="1395233222842">
<node TEXT="GH156 - Inject configuration parameters to client" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1568067492" CREATED="1394794029924" MODIFIED="1395233220331" LINK="https://github.com/flower-platform/flower-platform-4/issues/156">
<node TEXT="take them from config maybe; increment a config counter" ID="ID_765026924" CREATED="1394794064391" MODIFIED="1394794082814"/>
<node TEXT="on Flex client init: check version + config counter / version" ID="ID_869860480" CREATED="1394794083139" MODIFIED="1394794103628"/>
<node TEXT="maybe check this during polling for updates?" ID="ID_162452882" CREATED="1394794119758" MODIFIED="1394794130675"/>
<node TEXT="Usable for pollint timer" ID="ID_1559770880" CREATED="1395233075213" MODIFIED="1395233111085"/>
<node TEXT="Use &quot;client.&quot;..." ID="ID_1246598871" CREATED="1395233126509" MODIFIED="1395233154982"/>
</node>
</node>
<node TEXT="Loading Splash Screen" STYLE_REF="Done 2 (Light Blue)" FOLDED="true" ID="ID_1043602183" CREATED="1395326111886" MODIFIED="1397745353228">
<node TEXT="GH163 - Loading Splash Screen" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1077893363" CREATED="1395326111886" MODIFIED="1395326131687" LINK="https://github.com/flower-platform/flower-platform-4/issues/163">
<node TEXT="Redmine" ID="ID_487488464" CREATED="1396018216201" MODIFIED="1396018216231">
<node TEXT="RM9231 - GH163 - Loading Splash Screen" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_710741805" CREATED="1396018216241" MODIFIED="1396018216271" LINK="http://csp1/redmine/issues/9231"/>
<node TEXT="RM9232 - [Review] GH163 - Loading Splash Screen" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_893755469" CREATED="1396018216601" MODIFIED="1396018216611" LINK="http://csp1/redmine/issues/9232"/>
</node>
<node TEXT="mesaj internationalizat" ID="ID_757218643" CREATED="1397644138229" MODIFIED="1397644143362"/>
</node>
</node>
<node TEXT="Preferences" STYLE_REF="Planned (Orange)" ID="ID_905470530" CREATED="1397552435200" MODIFIED="1397552542322">
<node TEXT="GH241 - Preferences persistence and interpretation on client side" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1370093976" CREATED="1397552509250" MODIFIED="1397552553350" LINK="https://github.com/flower-platform/flower-platform-4/issues/241">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_1063600469" STARTINCLINATION="2038;0;" ENDINCLINATION="2038;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
<node TEXT="GH242 - Preferences Editor" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_873354994" CREATED="1397552481640" MODIFIED="1397657409223" LINK="https://github.com/flower-platform/flower-platform-4/issues/242">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_1063600469" STARTINCLINATION="2437;-349;" ENDINCLINATION="1571;-349;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
</node>
<node TEXT="GH134 - Use content cache for all icons" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1615267349" CREATED="1395134912611" MODIFIED="1395137237700" LINK="https://github.com/flower-platform/flower-platform-4/issues/134">
<node TEXT="Acum doar in editorul de iconite pare sa mearga." ID="ID_564610524" CREATED="1395134922207" MODIFIED="1395134941502"/>
<node TEXT="In rest, e.g. actiuni (meniu si buton) nu merg" ID="ID_1687000279" CREATED="1395134941882" MODIFIED="1395134952509"/>
<node TEXT="Redmine" FOLDED="true" ID="ID_1165509452" CREATED="1396347177909" MODIFIED="1396347177919">
<node TEXT="RM9242 - GH134 - Use content cache for all icons" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1594364056" CREATED="1396347177919" MODIFIED="1396347177949" LINK="http://csp1/redmine/issues/9242"/>
<node TEXT="RM9243 - [Review] GH134 - Use content cache for all icons" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1151985617" CREATED="1396347178549" MODIFIED="1396347178549" LINK="http://csp1/redmine/issues/9243"/>
</node>
</node>
<node TEXT="GH135 - Generic UI for adding nodes" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1030763785" CREATED="1393932142165" MODIFIED="1395137262351" LINK="https://github.com/flower-platform/flower-platform-4/issues/135">
<node TEXT="Redmine" ID="ID_562374837" CREATED="1395743980071" MODIFIED="1395743980081">
<node TEXT="RM9200 - GH135 - Generic UI for adding nodes" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_528860998" CREATED="1395743980081" MODIFIED="1395743980111" LINK="http://csp1/redmine/issues/9200"/>
<node TEXT="RM9201 - [Review] GH135 - Generic UI for adding nodes" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1535509289" CREATED="1395743980631" MODIFIED="1395743980641" LINK="http://csp1/redmine/issues/9201"/>
</node>
<node TEXT="Construction site" ID="ID_104026251" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="Am putea avea descriptori de proprietati, care zic: &quot;eu particip la creare&quot;;" ID="ID_1579329476" CREATED="1393932717444" MODIFIED="1393932754535"/>
<node TEXT="+ sa zica: eu sunt optional; eu sunt obligatoriu" ID="ID_1370102204" CREATED="1393932754974" MODIFIED="1393932891025">
<node TEXT="..." ID="ID_1163805671" CREATED="1395744174834" MODIFIED="1395744224726">
<node TEXT="&quot;eu particip la creare&quot;;" ID="ID_1744648942" CREATED="1395744232314" MODIFIED="1395744233086"/>
</node>
<node TEXT="isMandatory" ID="ID_1205081842" CREATED="1395744058065" MODIFIED="1395744083104">
<node TEXT="false" ID="ID_1772557820" CREATED="1395744116034" MODIFIED="1395744119566"/>
</node>
</node>
<node TEXT="Am putea chiar sa punem toti descriptorii. Si chiar daca nu sunt prelucrati de add, sa se faca apoi setProp" ID="ID_534000340" CREATED="1393932922974" MODIFIED="1393932952145"/>
<node TEXT="Daca am avea props obligatorii, atunci afisam dialogul. Altfel, afisam doar daca se doreste explicit (o alta actiune, ...)" ID="ID_1563698621" CREATED="1393932960195" MODIFIED="1393932994116"/>
<node TEXT="acum e hardcodat cel de fisier&#xa;acum exista un &quot;properties&quot; in descriptor. Dar nu e ce vrem noi. E folosit pentru a pune un boolean &quot;isDirectory&quot;" ID="ID_99479130" CREATED="1394724170984" MODIFIED="1394724176947"/>
<node TEXT="AddNodeAction" ID="ID_1902016517" CREATED="1395744248704" MODIFIED="1395744256276">
<node TEXT="verificam daca node are descriptori de creare obligatorii" ID="ID_1874328324" CREATED="1395744257224" MODIFIED="1395744352634">
<node TEXT="da -&gt; afisam dialog" ID="ID_19581840" CREATED="1395744353502" MODIFIED="1395744360404"/>
<node TEXT="nu -&gt; addChild" ID="ID_1906503407" CREATED="1395744360892" MODIFIED="1395744366264"/>
</node>
<node TEXT="CreateNodeView" ID="ID_1966617825" CREATED="1395744385109" MODIFIED="1395744396741">
<node TEXT="showOptional" ID="ID_960639124" CREATED="1395744431569" MODIFIED="1395744461711"/>
<node TEXT="showAll" ID="ID_975063045" CREATED="1395744462089" MODIFIED="1395744466071"/>
<node TEXT="la sfarsit -&gt;" ID="ID_116691901" CREATED="1395744592621" MODIFIED="1395744678723">
<node ID="ID_446750176" CREATED="1395744669253" MODIFIED="1395744669253"><richcontent TYPE="NODE">

<html>
  <head>
    
  </head>
  <body>
    <p>
      addChild(.. properties);
    </p>
  </body>
</html>
</richcontent>
</node>
<node TEXT="if showAll -&gt; setProperty(...)" ID="ID_1600267712" CREATED="1395744660851" MODIFIED="1395744691653"/>
</node>
</node>
</node>
<node TEXT="delete CreateFile..View" ID="ID_448689042" CREATED="1395744703081" MODIFIED="1395744709703"/>
<node TEXT="delete AddChildDescriptor.properties" ID="ID_376260179" CREATED="1395744710541" MODIFIED="1395744719593"/>
</node>
</node>
<node TEXT="GH143 - Icons: embed &amp; pluginize" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1442123077" CREATED="1395138162619" MODIFIED="1395138517594" LINK="https://github.com/flower-platform/flower-platform-4/issues/143">
<node TEXT=".flex_client.icons" ID="ID_373251386" CREATED="1395138224471" MODIFIED="1395138229747">
<node TEXT="aici punem noi iconite" ID="ID_23052570" CREATED="1395138230674" MODIFIED="1395138235768"/>
<node TEXT="daca fac terti, ei isi pot face si ei plugin de iconite, sau sa le ia dinamic" ID="ID_827414771" CREATED="1395138236227" MODIFIED="1395138372604"/>
<node TEXT="facem conventie: sa scriem de unde este + cine foloseste" ID="ID_1904082485" CREATED="1395138380893" MODIFIED="1395138400251"/>
<node TEXT="Vedem ce bla bla cu licenta" ID="ID_680247384" CREATED="1395138408597" MODIFIED="1395138417675"/>
<node TEXT="Astfel putem refolosi iconite din freeplane; acel plugin il vom licentia mereu GPL" ID="ID_1714640439" CREATED="1395138418379" MODIFIED="1395138447951"/>
</node>
<node TEXT="de facut idem si pentru mesaje. Astfel nu vom mai avea nevoie de listeneri ca mesajele au fost incarcata" ID="ID_1452422832" CREATED="1395163982011" MODIFIED="1395164006563"/>
<node TEXT=".resources" ID="ID_1787734584" CREATED="1396017103167" MODIFIED="1396017112579">
<node TEXT="images" STYLE_REF="Planned (Orange)" ID="ID_315032082" CREATED="1396017114167" MODIFIED="1396017193659"/>
<node TEXT="messages" STYLE_REF="Planned (Orange)" ID="ID_910556727" CREATED="1396017181737" MODIFIED="1396017193659">
<node TEXT="messages.properties" ID="ID_994548934" CREATED="1396017130087" MODIFIED="1396017139659"/>
</node>
<node TEXT="e plugin java" ID="ID_1768988052" CREATED="1396017162047" MODIFIED="1396017165419"/>
<node TEXT="e src linked" ID="ID_587570116" CREATED="1396017165717" MODIFIED="1396017290748">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_377902356" STARTINCLINATION="40;0;" ENDINCLINATION="40;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
</node>
<node TEXT=".flex_client.resources" ID="ID_377902356" CREATED="1396017276737" MODIFIED="1396017283159">
<node TEXT="src" ID="ID_1608963924" CREATED="1396017359651" MODIFIED="1396017364293">
<node TEXT="Resources" ID="ID_451750612" CREATED="1396017365251" MODIFIED="1396017368753">
<node TEXT="getM" ID="ID_289868246" CREATED="1396017369293" MODIFIED="1396017376841"/>
<node TEXT="icon.." ID="ID_622318313" CREATED="1396017377379" MODIFIED="1396017379741"/>
</node>
</node>
</node>
<node TEXT="Redmine" ID="ID_601903469" CREATED="1396017545228" MODIFIED="1396017545238">
<node TEXT="RM9229 - GH143 - Icons: embed &amp; pluginize" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1272047481" CREATED="1396017545238" MODIFIED="1396017545258" LINK="http://csp1/redmine/issues/9229"/>
<node TEXT="RM9230 - [Review] GH143 - Icons: embed &amp; pluginize" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_95761" CREATED="1396017545818" MODIFIED="1396017545828" LINK="http://csp1/redmine/issues/9230"/>
</node>
</node>
<node TEXT="GH162 - Openable nodes (in new editor)" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_871512222" CREATED="1395320734829" MODIFIED="1395321163612" LINK="https://github.com/flower-platform/flower-platform-4/issues/162">
<node TEXT="proprietate: openable" ID="ID_1154442233" CREATED="1395320749695" MODIFIED="1395320764648"/>
<node TEXT="pusa pe nodul file" ID="ID_267687051" CREATED="1395320764863" MODIFIED="1395320769387"/>
<node TEXT="clientul: dc exista ac. propr" ID="ID_59472443" CREATED="1395320773040" MODIFIED="1395320791140">
<node TEXT="Instead of expand =&gt; dialog &quot;open in new editor?&quot;" ID="ID_253593465" CREATED="1395320800767" MODIFIED="1395320853084"/>
<node TEXT="with &quot;remeber setting&quot;" ID="ID_17434386" CREATED="1395320853353" MODIFIED="1395320862295"/>
<node TEXT="+ menu setting (&quot;expand&quot;)" ID="ID_928157677" CREATED="1395320863031" MODIFIED="1395320876973">
<node TEXT="TODO set per user, needs preferences system)" ID="ID_1785078859" CREATED="1395938453416" MODIFIED="1395938487654"/>
</node>
</node>
<node TEXT="Redmine" ID="ID_1828382038" CREATED="1395921935606" MODIFIED="1395921935606">
<node TEXT="RM9222 - GH162 - Openable nodes (in new editor)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1673062552" CREATED="1395921935616" MODIFIED="1395921935626" LINK="http://csp1/redmine/issues/9222"/>
<node TEXT="RM9223 - [Review] GH162 - Openable nodes (in new editor)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1700596619" CREATED="1395921935996" MODIFIED="1395921935996" LINK="http://csp1/redmine/issues/9223"/>
</node>
</node>
</node>
<node TEXT="flex_client.host_app" STYLE_REF="In progress (Yellow)" FOLDED="true" POSITION="right" ID="ID_457894412" CREATED="1390314235884" MODIFIED="1394460283243">
<node TEXT="GH10 - Investigate the high startup time of the plain Flex App" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1285784134" CREATED="1390238310946" MODIFIED="1392304351770" LINK="https://github.com/flower-platform/flower-platform-4/issues/10">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_999235185" CREATED="1390314269437" MODIFIED="1390314269437">
<node TEXT="RM8838 - GH10 - Investigate the high startup time of the plain Flex App" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_95126240" CREATED="1390314269437" MODIFIED="1390314269447" LINK="http://csp1/redmine/issues/8838"/>
<node TEXT="RM8839 - [Review] GH10 - Investigate the high startup time of the plain Flex App" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1929350544" CREATED="1390314269847" MODIFIED="1390314269857" LINK="http://csp1/redmine/issues/8839"/>
</node>
<node TEXT="Expected results" ID="ID_1054314594" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="Why startup is so long (3,5 s on my machine)?" ID="ID_69579846" CREATED="1390314292506" MODIFIED="1390314311437"/>
</node>
<node TEXT="Construction site" ID="ID_257450245" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="1st step: new tools setup" ID="ID_1830315863" CREATED="1390314366625" MODIFIED="1390314379867"/>
<node TEXT="Ideas" ID="ID_1647735220" CREATED="1390314344976" MODIFIED="1390314349267">
<node TEXT="About 3,5 sec (with Firebug) the messages file is requested." ID="ID_28727846" CREATED="1390238523407" MODIFIED="1390238552849"/>
<node TEXT="Try using the log framework in flex, to log time related messages" ID="ID_143532713" CREATED="1390238558348" MODIFIED="1390238578670"/>
<node TEXT="Is the plugin framework consuming?" ID="ID_1415906428" CREATED="1390238866961" MODIFIED="1390238885622"/>
<node TEXT="I see a usage peak of 9%. On my PC, 12% means 100% of a core." ID="ID_198852221" CREATED="1390238885840" MODIFIED="1390238911072"/>
<node TEXT="time a plain/new app" ID="ID_1562798738" CREATED="1390314085915" MODIFIED="1390314096096"/>
</node>
</node>
<node TEXT="Sincronizare&#xa;21/01/2014" STYLE_REF="Validated (Green)" ID="ID_167645" CREATED="1390454219176" MODIFIED="1390454249253">
<icon BUILTIN="group"/>
<node TEXT="begin" ID="ID_869251652" CREATED="1390454227170" MODIFIED="1390454233286"/>
</node>
<node TEXT="Sincronizare&#xa;22/01/2014" STYLE_REF="Validated (Green)" ID="ID_1798860606" CREATED="1390454221836" MODIFIED="1390454249284">
<icon BUILTIN="group"/>
<node TEXT="end" ID="ID_257674164" CREATED="1390454235664" MODIFIED="1390454236601"/>
</node>
</node>
</node>
<node TEXT="flex_client.host_app.mobile" STYLE_REF="In progress (Yellow)" FOLDED="true" POSITION="right" ID="ID_456884977" CREATED="1394459662878" MODIFIED="1395050234740">
<node TEXT="GH112 - Iteration 0: kick start" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1841905648" CREATED="1394459671526" MODIFIED="1394459996631" LINK="https://github.com/flower-platform/flower-platform-4/issues/112">
<node TEXT="Redmine" ID="ID_1276769112" CREATED="1394551248756" MODIFIED="1394551248762">
<node TEXT="RM9104 - GH112 - Iteration 0: kick start" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1226837699" CREATED="1394551248764" MODIFIED="1394551248791" LINK="http://csp1/redmine/issues/9104"/>
<node TEXT="RM9105 - [Review] GH112 - Iteration 0: kick start" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1172090647" CREATED="1394551249312" MODIFIED="1394551249319" LINK="http://csp1/redmine/issues/9105"/>
</node>
</node>
<node TEXT="Server Accounts Management" STYLE_REF="Planned (Orange)" ID="ID_1374056562" CREATED="1394712126431" MODIFIED="1394712154280">
<node TEXT="Server Accounts screen" ID="ID_1649073761" CREATED="1394710923146" MODIFIED="1394711325304">
<node TEXT="Actions" ID="ID_1331526931" CREATED="1394711313236" MODIFIED="1394711332245">
<node TEXT="Add Server Account" ID="ID_1799753457" CREATED="1394711333285" MODIFIED="1394711342586">
<node TEXT="on click =&gt; open the Edit Server Account screen" ID="ID_1579989391" CREATED="1394711478507" MODIFIED="1394711496867"/>
</node>
</node>
<node TEXT="Renderer" ID="ID_1988089434" CREATED="1394711344873" MODIFIED="1394711347813">
<node TEXT="Text: userName @ frientlyName (or host if empty)" ID="ID_1838158587" CREATED="1394711350445" MODIFIED="1394711456156">
<node TEXT="e.g." ID="ID_352244509" CREATED="1394711385954" MODIFIED="1394711388688">
<node TEXT="cristi @ WorkServer" ID="ID_1454914282" CREATED="1394711389751" MODIFIED="1394711399601"/>
<node TEXT="cristi @ myIp:8080/flower" ID="ID_942829505" CREATED="1394711399958" MODIFIED="1394711422739"/>
</node>
</node>
<node TEXT="Checkbox for the default one" ID="ID_1747868485" CREATED="1394711430924" MODIFIED="1394711445479">
<node TEXT="on click =&gt; make the current default; everything else is not default" ID="ID_340188668" CREATED="1394711502512" MODIFIED="1394711550252"/>
</node>
<node TEXT="on click =&gt; open the Edit Server Account screen" ID="ID_1837960407" CREATED="1394711478507" MODIFIED="1394711496867"/>
</node>
</node>
<node TEXT="Edit Server Account" ID="ID_389595533" CREATED="1394711559696" MODIFIED="1394711747404">
<node TEXT="Title" ID="ID_1372814709" CREATED="1394711747857" MODIFIED="1394711765370">
<node TEXT="If new =&gt; New Server Account" ID="ID_764434759" CREATED="1394711751744" MODIFIED="1394711760829"/>
</node>
<node TEXT="Actions" ID="ID_1047379304" CREATED="1394711761943" MODIFIED="1394711763342">
<node TEXT="If ! new =&gt; Remove Server Account" ID="ID_1502300359" CREATED="1394711766567" MODIFIED="1394711779357">
<node TEXT="on click =&gt; confirm then delete" ID="ID_912639145" CREATED="1394711780191" MODIFIED="1394711794914"/>
</node>
</node>
<node TEXT="Fields" ID="ID_1381965208" CREATED="1394711799609" MODIFIED="1394711802650">
<node TEXT="Friendly Name" ID="ID_814082767" CREATED="1394711803231" MODIFIED="1394711807478"/>
<node TEXT="Host" ID="ID_1007377503" CREATED="1394711807843" MODIFIED="1394711811240">
<node TEXT="mandatory" ID="ID_1250870949" CREATED="1394711821880" MODIFIED="1394711823671"/>
</node>
<node TEXT="User" ID="ID_341035791" CREATED="1394711813001" MODIFIED="1394711814563"/>
<node TEXT="Password" ID="ID_1782178903" CREATED="1394711815919" MODIFIED="1394711820131">
<node TEXT="pass" ID="ID_391791157" CREATED="1394711825840" MODIFIED="1394711828266"/>
</node>
</node>
<node TEXT="Buttons" ID="ID_625065823" CREATED="1394711843482" MODIFIED="1394711845838">
<node TEXT="Save" ID="ID_1053770701" CREATED="1394711846276" MODIFIED="1394711847131">
<node TEXT="on click =&gt; if new =&gt; question: Would you like to make this server account the default one? If yes:" ID="ID_915773043" CREATED="1394711850911" MODIFIED="1394711906591">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_340188668" STARTINCLINATION="212;0;" ENDINCLINATION="212;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
</node>
<node TEXT="Cancel" ID="ID_1531423104" CREATED="1394711847385" MODIFIED="1394711849612"/>
</node>
</node>
<node TEXT="On app startup" ID="ID_1958248114" CREATED="1394711938194" MODIFIED="1394711946887">
<node TEXT="If there is no account" ID="ID_576798537" CREATED="1394711947641" MODIFIED="1394711972466">
<node TEXT="Message: Please create a new server account:" ID="ID_696952716" CREATED="1394711972991" MODIFIED="1394712110697"/>
<node TEXT="show Edit Server Account Screen" ID="ID_297079597" CREATED="1394711997001" MODIFIED="1394712008984"/>
<node TEXT="After create =&gt; connect" ID="ID_257990278" CREATED="1394712050791" MODIFIED="1394712056148"/>
</node>
<node TEXT="If no default" ID="ID_481393302" CREATED="1394712010266" MODIFIED="1394712017443">
<node TEXT="Message: Please select which account you want to use:" ID="ID_510270649" CREATED="1394711972991" MODIFIED="1394712107274"/>
<node TEXT="show Server Accounts screen" ID="ID_870970275" CREATED="1394711997001" MODIFIED="1394712078039"/>
<node TEXT="on click =&gt; connect" ID="ID_128556505" CREATED="1394712114705" MODIFIED="1394712120129"/>
</node>
</node>
<node TEXT="GH113 - Connection configuration and local storage" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_900220885" CREATED="1394459955230" MODIFIED="1394459999581" LINK="https://github.com/flower-platform/flower-platform-4/issues/113">
<node TEXT="Redmine" ID="ID_1702353817" CREATED="1394711109677" MODIFIED="1394711109677">
<node TEXT="RM9118 - GH113 - Connection configuration and local storage" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_683466978" CREATED="1394711109677" MODIFIED="1394711109697" LINK="http://csp1/redmine/issues/9118"/>
<node TEXT="RM9119 - [Review] GH113 - Connection configuration and local storage" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_635021436" CREATED="1394711110037" MODIFIED="1394711110047" LINK="http://csp1/redmine/issues/9119"/>
</node>
</node>
</node>
<node TEXT="Global menu" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_802128611" CREATED="1395165691014" MODIFIED="1395165700896">
<node TEXT="GH131 - Global menu" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1406489199" CREATED="1394810339686" MODIFIED="1395165789221" LINK="https://github.com/flower-platform/flower-platform-4/issues/131">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_292243019" CREATED="1394811521883" MODIFIED="1394811521894">
<node TEXT="RM9132 - GH131 - Global menu" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1966244987" CREATED="1394811521896" MODIFIED="1394811521929" LINK="http://csp1/redmine/issues/9132"/>
<node TEXT="RM9133 - [Review] GH131 - Global menu" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_326716570" CREATED="1394811522584" MODIFIED="1394811522592" LINK="http://csp1/redmine/issues/9133"/>
</node>
</node>
</node>
<node TEXT="GH154 - Publish app to Play Store" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1952475117" CREATED="1395232419299" MODIFIED="1395232938408" LINK="https://github.com/flower-platform/flower-platform-4/issues/154">
<node TEXT="Redmine" ID="ID_645149689" CREATED="1396941679382" MODIFIED="1396941679392">
<node TEXT="RM9286 - GH154 - Publish app to Play Store" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_298079809" CREATED="1396941679392" MODIFIED="1396941679422" LINK="http://csp1/redmine/issues/9286"/>
<node TEXT="RM9287 - [Review] GH154 - Publish app to Play Store" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1205879291" CREATED="1396941679742" MODIFIED="1396941679752" LINK="http://csp1/redmine/issues/9287"/>
</node>
<node TEXT="Construction site" ID="ID_224960521" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="Steps" ID="ID_1191841949" CREATED="1396945053880" MODIFIED="1396945056891">
<node TEXT="1. Create a self-signed certificate" ID="ID_880943556" CREATED="1396945061350" MODIFIED="1396945133363">
<node TEXT="Properties &gt; Flex Build Packaging &gt; Google Android." ID="ID_720852609" CREATED="1396945134182" MODIFIED="1396945139283"/>
</node>
<node TEXT="2.  Modify the application descriptor file (.xml)" ID="ID_1603714346" CREATED="1396945191322" MODIFIED="1396945198113"/>
<node TEXT="3. Create a release build of the application" ID="ID_1930017756" CREATED="1396945581223" MODIFIED="1396945583745">
<node TEXT="Project &gt; Export Release Build" ID="ID_647548981" CREATED="1396945592674" MODIFIED="1396945593155"/>
</node>
<node TEXT="4. Register for a publisher account" ID="ID_1222828312" CREATED="1396945665554" MODIFIED="1396945693785"/>
<node TEXT="5. Publish" ID="ID_1082387644" CREATED="1396945860335" MODIFIED="1396945864346"/>
</node>
<node TEXT="Doc" ID="ID_9634871" CREATED="1396945713304" MODIFIED="1396945719485">
<node TEXT="http://www.adobe.com/devnet/air/articles/packaging-air-apps-android.html" ID="ID_1418051475" CREATED="1396945720534" MODIFIED="1396945726705" LINK="http://www.adobe.com/devnet/air/articles/packaging-air-apps-android.html"/>
<node TEXT="http://developer.android.com/distribute/googleplay/publish/register.html" ID="ID_1211818946" CREATED="1396945733324" MODIFIED="1396945738165" LINK="http://developer.android.com/distribute/googleplay/publish/register.html"/>
<node TEXT="http://developer.android.com/distribute/googleplay/publish/console.html" ID="ID_1376400873" CREATED="1396945840305" MODIFIED="1396945847216" LINK="http://developer.android.com/distribute/googleplay/publish/console.html"/>
<node TEXT="https://play.google.com/apps/publish/" ID="ID_1407502748" CREATED="1396945878585" MODIFIED="1396945884316" LINK="https://play.google.com/apps/publish/"/>
</node>
</node>
</node>
</node>
<node TEXT="flex_client.mindmap" STYLE_REF="In progress (Yellow)" FOLDED="true" POSITION="right" ID="ID_1585303537" CREATED="1392370603945" MODIFIED="1394116524178">
<node TEXT="GH137 - Refactor move editor mindmap from core -&gt; mindmap" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_885055448" CREATED="1393576263577" MODIFIED="1397652549134" LINK="https://github.com/flower-platform/flower-platform-4/issues/137">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_1312717178" CREATED="1395417170757" MODIFIED="1395417170787">
<node TEXT="RM9188 - GH137 - Refactor move editor mindmap from core -&gt; mindmap" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_893999070" CREATED="1395417170787" MODIFIED="1395417170817" LINK="http://csp1/redmine/issues/9188"/>
<node TEXT="RM9189 - [Review] GH137 - Refactor move editor mindmap from core -&gt; mindmap" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1956890202" CREATED="1395417171157" MODIFIED="1395417171157" LINK="http://csp1/redmine/issues/9189"/>
</node>
</node>
<node TEXT="GH204 - BUG: color picker -&gt; error when choosing a color starting with 00, uint value isn&apos;t properly transformed to hex" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1534286839" CREATED="1396505598409" MODIFIED="1396528621877" LINK="https://github.com/flower-platform/flower-platform-4/issues/204">
<node TEXT="Redmine" ID="ID_481569261" CREATED="1396858557662" MODIFIED="1396858557690">
<node TEXT="RM9271 - GH204 - BUG: color picker -&gt; error when choosing a color starting with 00, uint value isn&apos;t properly transformed to hex" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1234856785" CREATED="1396858557692" MODIFIED="1396858557717" LINK="http://csp1/redmine/issues/9271"/>
<node TEXT="RM9272 - [Review] GH204 - BUG: color picker -&gt; error when choosing a color starting with 00, uint value isn&apos;t properly transformed to hex" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1583964535" CREATED="1396858558044" MODIFIED="1396858558054" LINK="http://csp1/redmine/issues/9272"/>
</node>
</node>
<node TEXT="GH205 - Show expand button on hover (currently on select)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1445981918" CREATED="1396528958308" MODIFIED="1397544592917" LINK="https://github.com/flower-platform/flower-platform-4/issues/205">
<node TEXT="Redmine" ID="ID_327044172" CREATED="1396602446892" MODIFIED="1396602446902">
<node TEXT="RM9266 - GH205 - Show expand button on hover (currently on select)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_504576251" CREATED="1396602446902" MODIFIED="1396602446942" LINK="http://csp1/redmine/issues/9266"/>
<node TEXT="RM9267 - [Review] GH205 - Show expand button on hover (currently on select)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_106214029" CREATED="1396602447542" MODIFIED="1396602447542" LINK="http://csp1/redmine/issues/9267"/>
</node>
</node>
<node TEXT="GH207 - Font correction" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1309148590" CREATED="1396529305913" MODIFIED="1396529317841" LINK="https://github.com/flower-platform/flower-platform-4/issues/207">
<node TEXT="Redmine" ID="ID_1356023221" CREATED="1396862429255" MODIFIED="1396862429284">
<node TEXT="RM9273 - GH207 - Font correction" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1846046413" CREATED="1396862429287" MODIFIED="1396862429331" LINK="http://csp1/redmine/issues/9273"/>
<node TEXT="RM9274 - [Review] GH207 - Font correction" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_732456540" CREATED="1396862429705" MODIFIED="1396862429713" LINK="http://csp1/redmine/issues/9274"/>
</node>
</node>
<node TEXT="GH226 - Recycling issue -&gt; node positions aren&apos;t correctly calculated" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_913138411" CREATED="1397120053386" MODIFIED="1397120092423" LINK="https://github.com/flower-platform/flower-platform-4/issues/226">
<node TEXT="Redmine" ID="ID_1835268769" CREATED="1397120098940" MODIFIED="1397120098942">
<node TEXT="RM9301 - GH226 - Recycling issue -&gt; node positions aren&apos;t correctly calculated" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1586501090" CREATED="1397120098943" MODIFIED="1397120098949" LINK="http://csp1/redmine/issues/9301"/>
<node TEXT="RM9302 - [Review] GH226 - Recycling issue -&gt; node positions aren&apos;t correctly calculated" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1167046191" CREATED="1397120099698" MODIFIED="1397120099706" LINK="http://csp1/redmine/issues/9302"/>
</node>
</node>
<node TEXT="Visual styles" ID="ID_1911870072" CREATED="1398767219223" MODIFIED="1398767225738">
<node TEXT="Side" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_1050335356" CREATED="1395141909533" MODIFIED="1397651636928">
<node TEXT="The provider says &quot;enabled&quot; only if visible root = mm node" ID="ID_172641870" CREATED="1395141927256" MODIFIED="1395141980437"/>
<node TEXT="GH146 - Side" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_27824345" CREATED="1395141909533" MODIFIED="1397651624613" LINK="https://github.com/flower-platform/flower-platform-4/issues/146">
<icon BUILTIN="flag-green"/>
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_1176383195" STARTINCLINATION="710;0;" ENDINCLINATION="687;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
<node TEXT="Redmine" ID="ID_331545673" CREATED="1395733906193" MODIFIED="1395733906213">
<node TEXT="RM9195 - GH146 - Side" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1859874408" CREATED="1395733906223" MODIFIED="1395733906243" LINK="http://csp1/redmine/issues/9195"/>
<node TEXT="RM9196 - [Review] GH146 - Side" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1853373434" CREATED="1395733906863" MODIFIED="1395733906873" LINK="http://csp1/redmine/issues/9196"/>
</node>
<node TEXT="Cred ca putem sa renuntam la node.side (as). si sa folosim pe baza unui ValueProvider" ID="ID_820940507" CREATED="1395231810748" MODIFIED="1395231934555"/>
</node>
</node>
<node TEXT="Node limits" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_1316068944" CREATED="1392371371537" MODIFIED="1393927591434">
<node TEXT="GH60 - Node limits" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_811233108" CREATED="1392370293355" MODIFIED="1393926603407" LINK="https://github.com/flower-platform/flower-platform-4/issues/60">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" FOLDED="true" ID="ID_862301674" CREATED="1392371365068" MODIFIED="1392371365068">
<node TEXT="RM9012 - GH60 - Node limits" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1567866480" CREATED="1392371365068" MODIFIED="1392371365088" LINK="http://csp1/redmine/issues/9012"/>
<node TEXT="RM9013 - [Review] GH60 - Node limits" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_617206013" CREATED="1392371365408" MODIFIED="1392371365418" LINK="http://csp1/redmine/issues/9013"/>
</node>
</node>
</node>
<node TEXT="Icons" STYLE_REF="Validated (Green)" ID="ID_1982947702" CREATED="1392370316675" MODIFIED="1393927174362">
<icon BUILTIN="flag"/>
<node TEXT="View icons in the properties editor" STYLE_REF="Validated (Green)" ID="ID_29458851" CREATED="1393927063780" MODIFIED="1393927215742"/>
<node TEXT="Icons menu" STYLE_REF="New (Red)" ID="ID_846522437" CREATED="1393927209360" MODIFIED="1393927351122">
<node TEXT="Icons from table" STYLE_REF="New (Red)" FOLDED="true" ID="ID_1937104129" CREATED="1393927250040" MODIFIED="1393927545782" LINK="#ID_212570465">
<node TEXT="Invokes the Manage Icons dialog" ID="ID_659860542" CREATED="1393927261912" MODIFIED="1393927271892"/>
</node>
<node TEXT="Icons by category" STYLE_REF="New (Red)" ID="ID_1810485090" CREATED="1393927285190" MODIFIED="1393927545792" LINK="#ID_242749231"/>
<node TEXT="Progress icons" STYLE_REF="New (Red)" ID="ID_1780271969" CREATED="1393927305040" MODIFIED="1393927545792" LINK="#ID_1563608021"/>
</node>
<node TEXT="Custom icons" STYLE_REF="New (Red)" ID="ID_412382433" CREATED="1392370467442" MODIFIED="1393927555532">
<node TEXT="GH95 - Custom icons" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1072613265" CREATED="1392370467442" MODIFIED="1393927568313" LINK="https://github.com/flower-platform/flower-platform-4/issues/95"/>
</node>
<node TEXT="GH94 - Icons improvements" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_459561055" CREATED="1393927119800" MODIFIED="1393927140522" LINK="https://github.com/flower-platform/flower-platform-4/issues/94">
<node TEXT="#Expected results" ID="ID_65916268" CREATED="1389360413465" MODIFIED="1393927151622">
<icon BUILTIN="revision"/>
<node TEXT="Icons from table" ID="ID_212570465" CREATED="1393927539902" MODIFIED="1393927539912" LINK="#ID_1937104129"/>
<node TEXT="Icons by category" ID="ID_242749231" CREATED="1393927539922" MODIFIED="1393927539922" LINK="#ID_1810485090"/>
<node TEXT="Progress icons" ID="ID_1563608021" CREATED="1393927539932" MODIFIED="1393927539932" LINK="#ID_1780271969"/>
<node TEXT="Tooltips" ID="ID_950988211" CREATED="1393927539932" MODIFIED="1393927539932" LINK="#ID_1037166416"/>
<node TEXT="Shortcuts for some items" ID="ID_1709219738" CREATED="1393927539932" MODIFIED="1393927539932" LINK="#ID_1326599363"/>
<node TEXT="Configure icon shortcuts" ID="ID_653635597" CREATED="1393927539942" MODIFIED="1393927539942" LINK="#ID_748307923"/>
</node>
<node TEXT="Construction site" FOLDED="true" ID="ID_1604615832" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="try to reuse as much code with the renderer/dialog" ID="ID_1537029404" CREATED="1393926447857" MODIFIED="1393926485525"/>
</node>
</node>
<node TEXT="GH61 - Icons" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_990695645" CREATED="1392370316675" MODIFIED="1393926556492" LINK="https://github.com/flower-platform/flower-platform-4/issues/61">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" FOLDED="true" ID="ID_353665785" CREATED="1392903607747" MODIFIED="1392903607757">
<node TEXT="RM9040 - GH61 - Icons" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_982784810" CREATED="1392903607757" MODIFIED="1392903607797" LINK="http://csp1/redmine/issues/9040"/>
<node TEXT="RM9041 - [Review] GH61 - Icons" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_643166321" CREATED="1392903608317" MODIFIED="1392903608327" LINK="http://csp1/redmine/issues/9041"/>
</node>
</node>
</node>
<node TEXT="Node format" STYLE_REF="Validated (Green)" ID="ID_1364501349" CREATED="1393928401345" MODIFIED="1395141876675">
<node TEXT="Font" STYLE_REF="Validated (Green)" ID="ID_947215740" CREATED="1393928410904" MODIFIED="1395141287220" LINK="#ID_167037792" FORMAT_AS_HYPERLINK="false">
<icon BUILTIN="flag"/>
<node TEXT="Family" STYLE_REF="Validated (Green)" ID="ID_855335673" CREATED="1393928416674" MODIFIED="1395140915304"/>
<node TEXT="Size" STYLE_REF="Validated (Green)" ID="ID_1815676217" CREATED="1393928422314" MODIFIED="1395140915309"/>
<node TEXT="Bold" STYLE_REF="Validated (Green)" ID="ID_235439895" CREATED="1393928424714" MODIFIED="1395140915314"/>
<node TEXT="Italic" STYLE_REF="Validated (Green)" ID="ID_427262696" CREATED="1393928427384" MODIFIED="1395140915294"/>
<node TEXT="Hyperlink" STYLE_REF="New (Red)" ID="ID_1191938028" CREATED="1395140907229" MODIFIED="1395141023289" LINK="#ID_1023090475"/>
<node TEXT="GH96 - Font" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_709565670" CREATED="1393928410904" MODIFIED="1395141058918" LINK="https://github.com/flower-platform/flower-platform-4/issues/96">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_1259577264" CREATED="1394192496144" MODIFIED="1394192496144">
<node TEXT="RM9084 - GH96 - Font" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_39092705" CREATED="1394192496154" MODIFIED="1394192496154" LINK="http://csp1/redmine/issues/9084"/>
<node TEXT="RM9085 - [Review] GH96 - Font" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_387633478" CREATED="1394192496484" MODIFIED="1394192496494" LINK="http://csp1/redmine/issues/9085"/>
</node>
<node TEXT="Expected results#" ID="ID_1305719569" CREATED="1389360413465" MODIFIED="1393928539356">
<icon BUILTIN="revision"/>
<node TEXT="Font" ID="ID_167037792" CREATED="1393928507476" MODIFIED="1393928507486" LINK="#ID_947215740"/>
</node>
<node TEXT="Construction site" ID="ID_397640670" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="What does &quot;hyperlink&quot; mean?" ID="ID_1631213598" CREATED="1393928462124" MODIFIED="1393928480026"/>
</node>
</node>
</node>
<node TEXT="Colors" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_320090740" CREATED="1393928553474" MODIFIED="1395141113427" LINK="#ID_1273013426">
<node TEXT="Text" STYLE_REF="Validated (Green)" ID="ID_1579809198" CREATED="1393928556914" MODIFIED="1395141113439"/>
<node TEXT="Background" STYLE_REF="Validated (Green)" ID="ID_1584972632" CREATED="1393928561574" MODIFIED="1395141277068"/>
<node TEXT="GH97 - Colors" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_853801507" CREATED="1393928553474" MODIFIED="1395141106254" LINK="https://github.com/flower-platform/flower-platform-4/issues/97">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_1218490734" CREATED="1394450818655" MODIFIED="1394450818660">
<node TEXT="RM9090 - GH97 - Colors" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_460710012" CREATED="1394450818662" MODIFIED="1394450818712" LINK="http://csp1/redmine/issues/9090"/>
<node TEXT="RM9091 - [Review] GH97 - Colors" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1742358978" CREATED="1394450819227" MODIFIED="1394450819235" LINK="http://csp1/redmine/issues/9091"/>
</node>
<node TEXT="Expected results#" ID="ID_1380124120" CREATED="1389360413465" MODIFIED="1393928539356">
<icon BUILTIN="revision"/>
<node TEXT="Colors" ID="ID_1273013426" CREATED="1393928608386" MODIFIED="1393928608396" LINK="#ID_320090740"/>
</node>
<node TEXT="Construction site" ID="ID_316429969" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
</node>
</node>
</node>
<node TEXT="Cloud" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_803144419" CREATED="1393928632514" MODIFIED="1395141268530">
<node TEXT="Arc" STYLE_REF="New (Red)" ID="ID_1134571064" CREATED="1393928755354" MODIFIED="1393928836097" LINK="#ID_278682734"/>
<node TEXT="Star" STYLE_REF="New (Red)" ID="ID_738244438" CREATED="1393928759984" MODIFIED="1393928836107" LINK="#ID_20250369"/>
<node TEXT="Rectangle" STYLE_REF="Validated (Green)" ID="ID_299894638" CREATED="1393928748134" MODIFIED="1395141271161" LINK="#ID_1720193447"/>
<node TEXT="Rounded rectangle" STYLE_REF="Validated (Green)" ID="ID_1747021356" CREATED="1393928751334" MODIFIED="1395141271171" LINK="#ID_1007152191"/>
<node TEXT="GH98 - Rectangular Cloud" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1450174635" CREATED="1393928632514" MODIFIED="1395141265740" LINK="https://github.com/flower-platform/flower-platform-4/issues/98">
<icon BUILTIN="flag-green"/>
<node TEXT="Expected results#" FOLDED="true" ID="ID_388309567" CREATED="1389360413465" MODIFIED="1393928539356">
<icon BUILTIN="revision"/>
<node TEXT="Rounded rectangle" ID="ID_1007152191" CREATED="1393928786046" MODIFIED="1393928786056" LINK="#ID_1747021356"/>
<node TEXT="Rectangle" ID="ID_1720193447" CREATED="1393928786056" MODIFIED="1393928786056" LINK="#ID_299894638"/>
</node>
<node TEXT="Construction site" ID="ID_169193330" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
</node>
<node TEXT="Redmine" ID="ID_393630177" CREATED="1394462951985" MODIFIED="1394462951985">
<node TEXT="RM9095 - GH98 - Rectangular Cloud" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1968670391" CREATED="1394462951985" MODIFIED="1394462951985" LINK="http://csp1/redmine/issues/9095"/>
<node TEXT="RM9096 - [Review] GH98 - Rectangular Cloud" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_52397545" CREATED="1394462952595" MODIFIED="1394462952605" LINK="http://csp1/redmine/issues/9096"/>
</node>
</node>
<node TEXT="GH99 - Arc Cloud" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_489761329" CREATED="1393928698804" MODIFIED="1393928827387" LINK="https://github.com/flower-platform/flower-platform-4/issues/99">
<node TEXT="Expected results#" FOLDED="true" ID="ID_634049870" CREATED="1389360413465" MODIFIED="1393928539356">
<icon BUILTIN="revision"/>
<node TEXT="Arc" ID="ID_278682734" CREATED="1393928791996" MODIFIED="1393928792006" LINK="#ID_1134571064"/>
</node>
<node TEXT="Construction site" ID="ID_1594506249" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
</node>
</node>
<node TEXT="GH100 - Star Cloud" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_737943965" CREATED="1393928721944" MODIFIED="1393928830327" LINK="https://github.com/flower-platform/flower-platform-4/issues/100">
<node TEXT="Expected results#" FOLDED="true" ID="ID_1028447570" CREATED="1389360413465" MODIFIED="1393928539356">
<icon BUILTIN="revision"/>
<node TEXT="Star" ID="ID_20250369" CREATED="1393928799046" MODIFIED="1393928799056" LINK="#ID_738244438"/>
</node>
<node TEXT="Construction site" ID="ID_226627132" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
</node>
</node>
</node>
</node>
<node TEXT="HTML" STYLE_REF="Done (Blue)" FOLDED="true" ID="ID_1519253623" CREATED="1393928881245" MODIFIED="1395913219388">
<node TEXT="HTML Renderer" STYLE_REF="Done (Blue)" ID="ID_1095978527" CREATED="1393928883715" MODIFIED="1394811475800" LINK="#ID_859850088">
<node TEXT="GH101 - HTML Renderer" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1224173902" CREATED="1393928883715" MODIFIED="1393929007837" LINK="https://github.com/flower-platform/flower-platform-4/issues/101">
<node TEXT="Redmine" ID="ID_1035066203" CREATED="1394625931057" MODIFIED="1394625931081">
<node TEXT="RM9112 - GH101 - HTML Renderer" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1565728625" CREATED="1394625931082" MODIFIED="1394625931108" LINK="http://csp1/redmine/issues/9112"/>
<node TEXT="RM9113 - [Review] GH101 - HTML Renderer" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1953145869" CREATED="1394625931445" MODIFIED="1394625931454" LINK="http://csp1/redmine/issues/9113"/>
</node>
<node TEXT="Expected results#" ID="ID_1101578740" CREATED="1389360413465" MODIFIED="1393928969907">
<icon BUILTIN="revision"/>
<node TEXT="HTML Renderer" ID="ID_859850088" CREATED="1393929001107" MODIFIED="1393929001107" LINK="#ID_1095978527"/>
</node>
<node TEXT="Construction site" ID="ID_1790831271" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="Even without an advanced rich editor, we may have the dialog edit HTML (with syntax highlight) + a real time renderer." ID="ID_82427645" CREATED="1393929066495" MODIFIED="1393929109967"/>
</node>
</node>
<node TEXT="Editor with real time preview" STYLE_REF="New (Red)" ID="ID_1838650005" CREATED="1395141695351" MODIFIED="1395141768260">
<node TEXT="Checkbox: HTML" ID="ID_576250979" CREATED="1395141713370" MODIFIED="1395141718595">
<node TEXT="on check/uncheck: surround/unsurround the text with &lt;html&gt;" ID="ID_884356854" CREATED="1395141719531" MODIFIED="1395141740279"/>
<node TEXT="on text modified: if we detect &lt;html&gt;...&lt;/html&gt; =&gt; we check/uncheck the box automatically" ID="ID_1837223557" CREATED="1395141740730" MODIFIED="1395141765039"/>
</node>
</node>
</node>
<node TEXT="HTML Editor" STYLE_REF="New (Red)" ID="ID_521020981" CREATED="1393928885465" MODIFIED="1393929030887" LINK="#ID_1288456185">
<node TEXT="GH102 - HTML Editor" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1102911758" CREATED="1393928885465" MODIFIED="1393929011767" LINK="https://github.com/flower-platform/flower-platform-4/issues/102">
<node TEXT="Expected results#" FOLDED="true" ID="ID_573270328" CREATED="1389360413465" MODIFIED="1393928973137">
<icon BUILTIN="revision"/>
<node TEXT="HTML Editor" ID="ID_1288456185" CREATED="1393928996757" MODIFIED="1393928996767" LINK="#ID_521020981"/>
</node>
<node TEXT="Construction site" ID="ID_74399547" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
</node>
</node>
</node>
</node>
<node TEXT="Hyperlinks" STYLE_REF="New (Red)" FOLDED="true" ID="ID_722720086" CREATED="1395140980299" MODIFIED="1395141887645" LINK="#ID_247089299">
<node TEXT="GH145 - Hyperlinks" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_240916652" CREATED="1395140980299" MODIFIED="1395140996768" LINK="https://github.com/flower-platform/flower-platform-4/issues/145">
<node TEXT="Expected results#" ID="ID_1835222371" CREATED="1389360413465" MODIFIED="1393928969907">
<icon BUILTIN="revision"/>
<node TEXT="Hyperlink style" ID="ID_1023090475" CREATED="1395141023280" MODIFIED="1395141033599" LINK="#ID_1191938028"/>
<node TEXT="Hyperlinks" ID="ID_247089299" CREATED="1395141023295" MODIFIED="1395141023298" LINK="#ID_722720086"/>
</node>
<node TEXT="Construction site" ID="ID_1851803635" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
</node>
</node>
</node>
<node TEXT="Note" STYLE_REF="In progress (Yellow)" FOLDED="true" ID="ID_1827754147" CREATED="1397480503418" MODIFIED="1397543859275">
<node TEXT="GH236 - Note" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_17836290" CREATED="1397480295307" MODIFIED="1397480498358" LINK="https://github.com/flower-platform/flower-platform-4/issues/236">
<node TEXT="Redmine" ID="ID_1875330764" CREATED="1397480523568" MODIFIED="1397480523568">
<node TEXT="RM9320 - GH236 - Note" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_964784864" CREATED="1397480523578" MODIFIED="1397480523578" LINK="http://csp1/redmine/issues/9320"/>
<node TEXT="RM9321 - [Review] GH236 - Note" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_980783649" CREATED="1397480524218" MODIFIED="1397480524228" LINK="http://csp1/redmine/issues/9321"/>
</node>
<node TEXT="Construction site" ID="ID_202312960" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="property -&gt; note:String" ID="ID_1723452660" CREATED="1397484736149" MODIFIED="1397484881816">
<node TEXT="propertiesProvider" ID="ID_1393343911" CREATED="1397485200811" MODIFIED="1397485214528"/>
<node TEXT="propertySetter" ID="ID_1882731823" CREATED="1397485215691" MODIFIED="1397485222241"/>
</node>
<node TEXT="NoteModel -&gt; xml, html?" ID="ID_1929658489" CREATED="1397485172371" MODIFIED="1397485187521"/>
<node TEXT="EditNoteInDialogAction" ID="ID_1513083606" CREATED="1397485227251" MODIFIED="1397485240657">
<node TEXT="opens RichTextWithRenderer" ID="ID_136026792" CREATED="1397485483139" MODIFIED="1397485535120"/>
<node TEXT="Ok -&gt; setProperty(&quot;note&quot;, ...)" ID="ID_1047844508" CREATED="1397485587659" MODIFIED="1397485603488">
<node TEXT="-&gt; set edit icon in ICONS switch case if note exists" ID="ID_337095197" CREATED="1397485842267" MODIFIED="1397486102272"/>
</node>
</node>
<node TEXT="NoteComponentExtension" ID="ID_132668670" CREATED="1397486292835" MODIFIED="1397486312337">
<node TEXT="TextArea" ID="ID_500771939" CREATED="1397486315987" MODIFIED="1397486320848"/>
</node>
</node>
</node>
</node>
<node TEXT="Node Details" STYLE_REF="Planned (Orange)" ID="ID_250263439" CREATED="1397659441749" MODIFIED="1397659476648">
<node TEXT="GH245 - Node Details" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1762718783" CREATED="1397659441749" MODIFIED="1397659459361" LINK="https://github.com/flower-platform/flower-platform-4/issues/245">
<node TEXT="Redmine" ID="ID_1418261932" CREATED="1397659464249" MODIFIED="1397659464251">
<node TEXT="RM9336 - GH245 - Node Details" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_340986555" CREATED="1397659464252" MODIFIED="1397659464257" LINK="http://csp1/redmine/issues/9336"/>
<node TEXT="RM9337 - [Review] GH245 - Node Details" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_634388482" CREATED="1397659464584" MODIFIED="1397659464592" LINK="http://csp1/redmine/issues/9337"/>
</node>
</node>
</node>
<node TEXT="Edges" STYLE_REF="Planned (Orange)" FOLDED="true" ID="ID_1223059473" CREATED="1398175985522" MODIFIED="1398176041418">
<node TEXT="GH250 - Edges" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_234800352" CREATED="1398175877929" MODIFIED="1398175969425" LINK="https://github.com/flower-platform/flower-platform-4/issues/250">
<node TEXT="Redmine" ID="ID_1710483436" CREATED="1398176014481" MODIFIED="1398176014483">
<node TEXT="RM9343 - GH250 - Edges" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1474553766" CREATED="1398176014484" MODIFIED="1398176014493" LINK="http://csp1/redmine/issues/9343"/>
<node TEXT="RM9344 - [Review] GH250 - Edges" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_990556384" CREATED="1398176015034" MODIFIED="1398176015038" LINK="http://csp1/redmine/issues/9344"/>
</node>
</node>
</node>
</node>
<node TEXT="Apply Style" STYLE_REF="Done (Blue)" ID="ID_1504685930" CREATED="1397039879245" MODIFIED="1397543941430">
<node TEXT="GH222 - Apply Style" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_861617157" CREATED="1397039838343" MODIFIED="1397039872975" LINK="https://github.com/flower-platform/flower-platform-4/issues/222">
<node TEXT="Expected results" ID="ID_86775498" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="entry in Properties -&gt; Style, as dropdown list" LOCALIZED_STYLE_REF="default" ID="ID_1538191962" CREATED="1397039913475" MODIFIED="1397040012526"/>
</node>
<node TEXT="Redmine" ID="ID_1736701376" CREATED="1397461965027" MODIFIED="1397461965096">
<node TEXT="RM9312 - GH222 - Apply Style" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1708677293" CREATED="1397461965099" MODIFIED="1397461965137" LINK="http://csp1/redmine/issues/9312"/>
<node TEXT="RM9313 - [Review] GH222 - Apply Style" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_504554244" CREATED="1397461965572" MODIFIED="1397461965580" LINK="http://csp1/redmine/issues/9313"/>
</node>
</node>
</node>
<node TEXT="New Node" STYLE_REF="Planned (Orange)" ID="ID_1611437530" CREATED="1398766726024" MODIFIED="1398766738914">
<node TEXT="On node open: select" ID="ID_1016851658" CREATED="1398766707959" MODIFIED="1398766713166"/>
<node TEXT="On node closed: open + above" ID="ID_1105175210" CREATED="1398766699287" MODIFIED="1398766707365"/>
<node TEXT="GH255 - New node" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_830885842" CREATED="1397647049593" MODIFIED="1398766721300" LINK="https://github.com/flower-platform/flower-platform-4/issues/255"/>
</node>
<node TEXT="Icons Side Bar" STYLE_REF="In progress (Yellow)" ID="ID_842220966" CREATED="1393926289621" MODIFIED="1398767259536">
<node TEXT="Displayed only in the mindmap editor (not in generic mindmaps)" ID="ID_1819041017" CREATED="1393926357803" MODIFIED="1393926392120"/>
<node TEXT="GH93 - Icons Side Bar" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1677524607" CREATED="1393926289621" MODIFIED="1393926445608" LINK="https://github.com/flower-platform/flower-platform-4/issues/93">
<node TEXT="Expected results" ID="ID_1355140809" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
</node>
<node TEXT="Construction site" ID="ID_209574997" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="try to reuse as much code with the renderer/dialog" ID="ID_360758077" CREATED="1393926447857" MODIFIED="1393926485525"/>
</node>
<node TEXT="Redmine" ID="ID_1251605513" CREATED="1394453796963" MODIFIED="1394453796983">
<node TEXT="RM9093 - GH93 - Icons Side Bar" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_855631535" CREATED="1394453796983" MODIFIED="1394453797003" LINK="http://csp1/redmine/issues/9093"/>
<node TEXT="RM9094 - [Review] GH93 - Icons Side Bar" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_411637003" CREATED="1394453797533" MODIFIED="1394453797533" LINK="http://csp1/redmine/issues/9094"/>
</node>
</node>
<node TEXT="GH257 - Improvements for sidebar" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1788502088" CREATED="1395160694493" MODIFIED="1398767256378" LINK="https://github.com/flower-platform/flower-platform-4/issues/257">
<node TEXT="Use a list or datarenderer with virtualized layout" ID="ID_135964117" CREATED="1395160697483" MODIFIED="1395160712365"/>
<node TEXT="Some buttons are bigger (on mobile)" ID="ID_1711209952" CREATED="1395160728133" MODIFIED="1398766917125"/>
<node TEXT="Mobile: On scroll, the actions are redrawing. Selection is changed?" ID="ID_442242974" CREATED="1395160735343" MODIFIED="1398766934298"/>
<node TEXT="Use style as link button" ID="ID_152318646" CREATED="1398766992238" MODIFIED="1398767011539"/>
</node>
</node>
<node TEXT="Manage Icons dialog" STYLE_REF="Validated (Green)" ID="ID_1915716561" CREATED="1393927020190" MODIFIED="1393927168022">
<node TEXT="Invoked from the properties editor" STYLE_REF="Validated (Green)" ID="ID_1836120530" CREATED="1393927052300" MODIFIED="1393927431642"/>
<node TEXT="Tooltips" STYLE_REF="New (Red)" ID="ID_1037166416" CREATED="1393927432750" MODIFIED="1393927545792" LINK="#ID_950988211"/>
<node TEXT="Shortcuts for some items" STYLE_REF="New (Red)" ID="ID_1326599363" CREATED="1393927448240" MODIFIED="1393927545802" LINK="#ID_1709219738"/>
<node TEXT="Configure icon shortcuts" STYLE_REF="New (Red)" ID="ID_748307923" CREATED="1393927498070" MODIFIED="1393927545802" LINK="#ID_653635597"/>
</node>
</node>
<node TEXT="flex_client.properties" STYLE_REF="In progress (Yellow)" FOLDED="true" POSITION="right" ID="ID_768145291" CREATED="1390465029457" MODIFIED="1392722117304">
<node TEXT="GH12 - Properties editor: first iteration" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1417861087" CREATED="1390465178998" MODIFIED="1393667297678" LINK="https://github.com/flower-platform/flower-platform-4/issues/12">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_140785405" CREATED="1390818020002" MODIFIED="1390818020018">
<node TEXT="RM8899 - GH12 - Properties editor: first iteration" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1482466796" CREATED="1390818020020" MODIFIED="1390818020045" LINK="http://csp1/redmine/issues/8899"/>
<node TEXT="RM8900 - [Review] GH12 - Properties editor: first iteration" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_848956439" CREATED="1390818020368" MODIFIED="1390818020378" LINK="http://csp1/redmine/issues/8900"/>
</node>
<node TEXT="Expected results" ID="ID_1864233561" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="properties for selected nodes" ID="ID_1653734140" CREATED="1390477755485" MODIFIED="1390477774176"/>
</node>
<node TEXT="Construction site" FOLDED="true" ID="ID_651878541" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="next" ID="ID_398893731" CREATED="1390476914133" MODIFIED="1390476915814">
<node TEXT="use an improved ui component, based on FormLayout" ID="ID_550019159" CREATED="1390476917203" MODIFIED="1390476934384"/>
<node TEXT="properties view for ide" ID="ID_571991527" CREATED="1390476935663" MODIFIED="1390476944284"/>
<node TEXT="node type based properties; e.g. freeplane: max width, icons" ID="ID_378799500" CREATED="1389868162093" MODIFIED="1389868181843"/>
<node TEXT="capture all properties/styles/data from xml into our node structure (persistable in DB). Be able to reproduce the whole file, even if we don&apos;t know how to handle all properties" ID="ID_600690326" CREATED="1390478369398" MODIFIED="1390478416209"/>
</node>
<node TEXT="remarci CS" ID="ID_1202847284" CREATED="1391429164658" MODIFIED="1391429167629">
<node ID="ID_525027840" CREATED="1391431214996" MODIFIED="1391431214996"><richcontent TYPE="NODE">

<html>
  <head>
    
  </head>
  <body>
    [discussion]<br/>remind the functions of the controllers
  </body>
</html>
</richcontent>
</node>
<node TEXT="Arborescenta de ContrProv, astfel incat poate nu tr sa punem in DiagramShell" ID="ID_1004820856" CREATED="1391429268317" MODIFIED="1391429302039"/>
<node TEXT="decorare client side? content cache?" ID="ID_1886799887" CREATED="1391431489156" MODIFIED="1391431495238"/>
</node>
</node>
</node>
<node TEXT="GH70 - Improvement using a Layout instead of the DataGroup" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1417040669" CREATED="1392721801190" MODIFIED="1392721871812" LINK="https://github.com/flower-platform/flower-platform-4/issues/70">
<node TEXT="Expected results" ID="ID_1374363065" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
<node TEXT="Numeric field for int" ID="ID_636004349" CREATED="1392721908180" MODIFIED="1392721930903"/>
<node TEXT="Using layout" ID="ID_811924406" CREATED="1392721893691" MODIFIED="1392721898602"/>
<node TEXT="text fields: commit on enter" ID="ID_231914711" CREATED="1392721898831" MODIFIED="1392721907362"/>
</node>
<node TEXT="Construction site" ID="ID_1099246755" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
</node>
<node TEXT="Redmine" ID="ID_864399788" CREATED="1392721998363" MODIFIED="1392721998363">
<node TEXT="RM9020 - GH70 - Improvement using a Layout instead of the DataGroup" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1666867272" CREATED="1392721998363" MODIFIED="1392721998363" LINK="http://csp1/redmine/issues/9020"/>
<node TEXT="RM9021 - [Review] GH70 - Improvement using a Layout instead of the DataGroup" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_269223636" CREATED="1392721998703" MODIFIED="1392721998713" LINK="http://csp1/redmine/issues/9021"/>
</node>
</node>
<node TEXT="Categories" STYLE_REF="Done (Blue)" ID="ID_675014137" CREATED="1392722016369" MODIFIED="1397543946301">
<node TEXT="GH71 - Categories" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_85833823" CREATED="1392722016369" MODIFIED="1392722030826" LINK="https://github.com/flower-platform/flower-platform-4/issues/71">
<node TEXT="Redmine" ID="ID_925342433" CREATED="1393493537733" MODIFIED="1393493537753">
<node TEXT="RM9063 - GH71 - Categories" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_991377677" CREATED="1393493537753" MODIFIED="1393493537773" LINK="http://csp1/redmine/issues/9063"/>
<node TEXT="RM9064 - [Review] GH71 - Categories" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_171651364" CREATED="1393493538113" MODIFIED="1393493538113" LINK="http://csp1/redmine/issues/9064"/>
</node>
</node>
</node>
<node TEXT="Properties for styles" STYLE_REF="Done (Blue)" ID="ID_1745379593" CREATED="1392722090810" MODIFIED="1397543945520">
<node TEXT="GH72 - Properties for styles: &quot;Change&quot; checkbox" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_873290420" CREATED="1392722041514" MODIFIED="1392722105044" LINK="https://github.com/flower-platform/flower-platform-4/issues/72">
<node TEXT="Redmine" ID="ID_1373280014" CREATED="1393517270669" MODIFIED="1393517270689">
<node TEXT="RM9067 - GH72 - Properties for styles: &quot;Change&quot; checkbox" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_685054378" CREATED="1393517270689" MODIFIED="1393517270709" LINK="http://csp1/redmine/issues/9067"/>
<node TEXT="RM9068 - [Review] GH72 - Properties for styles: &quot;Change&quot; checkbox" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1847717152" CREATED="1393517271039" MODIFIED="1393517271039" LINK="http://csp1/redmine/issues/9068"/>
</node>
<node TEXT="Expected results" ID="ID_1303328543" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
</node>
<node TEXT="Construction site" FOLDED="true" ID="ID_119768837" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="propertyDescriptor.hasChangeCheckbox" ID="ID_1071217766" CREATED="1393579192847" MODIFIED="1393579209946"/>
<node TEXT="nodeType: mindMapStyle" ID="ID_1876781013" CREATED="1393579216086" MODIFIED="1393579570210"/>
<node TEXT="FreePlane MindMap: chProvider care populeaza cu noduri de stiluri" ID="ID_561603915" CREATED="1393579673899" MODIFIED="1393579690305"/>
<node TEXT="Node.getPropertyValue(propetyName)" STYLE_REF="Code" ID="ID_127162080" CREATED="1393579723836" MODIFIED="1393580001151">
<node TEXT="daca exista valoare =&gt; returnam" ID="ID_90824527" CREATED="1393580024313" MODIFIED="1393580031366"/>
<node TEXT="ne uitam la ce proprietate de stil" ID="ID_1507486777" CREATED="1393580003665" MODIFIED="1393580013438"/>
<node TEXT="si luam valoarea de acolo" ID="ID_777249682" CREATED="1393580042225" MODIFIED="1393580048286"/>
</node>
<node TEXT="checkbox.uncheck =&gt; set (default)" ID="ID_587248491" CREATED="1393580050360" MODIFIED="1393580059934"/>
</node>
</node>
</node>
</node>
<node TEXT="flex_client.server" STYLE_REF="Validated (Green)" FOLDED="true" POSITION="right" ID="ID_1792899984" CREATED="1391420466473" MODIFIED="1394709007159">
<node TEXT="Provide services (remote objects) from our service registry" STYLE_REF="Validated (Green)" ID="ID_507732937" CREATED="1391420472713" MODIFIED="1394709005749" LINK="#ID_1085051728">
<node TEXT="GH27 - Provide services (remote objects) from our service registry; Method invocation hooks" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1080916661" CREATED="1391420472713" MODIFIED="1392305418884" LINK="https://github.com/flower-platform/flower-platform-4/issues/27">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_668327749" CREATED="1391422203300" MODIFIED="1391422203300">
<node TEXT="RM8924 - GH27 - Provide services (remote objects) from our service registry; Method invocation hooks" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1687087897" CREATED="1391422203310" MODIFIED="1391422203310" LINK="http://csp1/redmine/issues/8924"/>
<node TEXT="RM8925 - [Review] GH27 - Provide services (remote objects) from our service registry; Method invocation hooks" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_560399045" CREATED="1391422203640" MODIFIED="1391422203650" LINK="http://csp1/redmine/issues/8925"/>
</node>
<node TEXT="#Expected results" ID="ID_791484046" CREATED="1389360413465" MODIFIED="1390558909010">
<icon BUILTIN="revision"/>
<node TEXT="Provide services (remote objects) from our service registry" ID="ID_1085051728" CREATED="1391421795211" MODIFIED="1391421795221" LINK="#ID_507732937"/>
<node TEXT="Service method invocation: pre and post invoke hooks" ID="ID_1488769316" CREATED="1391421795231" MODIFIED="1391421795231"/>
<node TEXT="log method call execution time" ID="ID_1162830544" CREATED="1391421814460" MODIFIED="1391421825321"/>
</node>
<node TEXT="Construction site" ID="ID_592160329" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="services-config -&gt; define factory" ID="ID_853081796" CREATED="1391420543603" MODIFIED="1391420555824"/>
<node TEXT="remoting-config -&gt; reference factory" ID="ID_43677591" CREATED="1391420556082" MODIFIED="1391420566394"/>
<node TEXT="JavaAdapter.invoke()" ID="ID_261572528" CREATED="1391421988004" MODIFIED="1391421994071">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_1488769316" STARTINCLINATION="231;0;" ENDINCLINATION="231;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
</node>
</node>
<node TEXT="GH51 - Get services directly from registry (no more need of remoting-config.xml)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1721116595" CREATED="1392126618131" MODIFIED="1394708965525" LINK="https://github.com/flower-platform/flower-platform-4/issues/51">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_663870813" CREATED="1392362136892" MODIFIED="1392362136924">
<node TEXT="RM9009 - GH51 - Get services directly from registry (no more need of remoting-config.xml)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1726900343" CREATED="1392362136925" MODIFIED="1392362136947" LINK="http://csp1/redmine/issues/9009"/>
<node TEXT="RM9010 - [Review] GH51 - Get services directly from registry (no more need of remoting-config.xml)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_507018885" CREATED="1392362137445" MODIFIED="1392362137447" LINK="http://csp1/redmine/issues/9010"/>
</node>
<node TEXT="#Expected results" ID="ID_1722866311" CREATED="1389360413465" MODIFIED="1390558909010">
<icon BUILTIN="revision"/>
</node>
<node TEXT="Construction site" ID="ID_1705186885" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="FlowerRemotingService" ID="ID_182887281" CREATED="1392126678826" MODIFIED="1392126687438"/>
<node TEXT=".createDestination" ID="ID_408679061" CREATED="1392126687946" MODIFIED="1392126691758"/>
</node>
</node>
</node>
<node TEXT="Remote method invocation listener" STYLE_REF="Validated (Green)" ID="ID_196410649" CREATED="1392126515541" MODIFIED="1392314862953">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_48532742" STARTINCLINATION="472;0;" ENDINCLINATION="494;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
<node TEXT="GH40 - Flex communication impossible after session expiration" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1137932649" CREATED="1391790702827" MODIFIED="1392653608704" LINK="https://github.com/flower-platform/flower-platform-4/issues/40">
<icon BUILTIN="flag-green"/>
<node TEXT="maybe related to the fact that we need to register some &quot;flex session listener...&quot;?" ID="ID_907969128" CREATED="1391790734337" MODIFIED="1391790750515"/>
<node TEXT="Redmine" ID="ID_819439857" CREATED="1392382665072" MODIFIED="1392382665078">
<node TEXT="RM9014 - GH40 - Flex communication impossible after session expiration" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_208713068" CREATED="1392382665080" MODIFIED="1392382665106" LINK="http://csp1/redmine/issues/9014"/>
<node TEXT="RM9015 - [Review] GH40 - Flex communication impossible after session expiration" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_661452035" CREATED="1392382665608" MODIFIED="1392382665612" LINK="http://csp1/redmine/issues/9015"/>
</node>
</node>
</node>
<node TEXT="flex_client.web" STYLE_REF="In progress (Yellow)" FOLDED="true" POSITION="right" ID="ID_1431472186" CREATED="1394810520630" MODIFIED="1395050201398">
<node TEXT="LinkHandler UI" STYLE_REF="Done (Blue)" ID="ID_474671111" CREATED="1394810529165" MODIFIED="1397039807365">
<node TEXT="GH132 - LinkHandler UI" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1521729442" CREATED="1394810529165" MODIFIED="1394810611868" LINK="https://github.com/flower-platform/flower-platform-4/issues/132">
<node TEXT="Redmine" ID="ID_1655633313" CREATED="1395050219040" MODIFIED="1395050219060">
<node TEXT="RM9136 - GH132 - LinkHandler UI" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1891055551" CREATED="1395050219060" MODIFIED="1395050219090" LINK="http://csp1/redmine/issues/9136"/>
<node TEXT="RM9137 - [Review] GH132 - LinkHandler UI" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_344870628" CREATED="1395050219560" MODIFIED="1395050219570" LINK="http://csp1/redmine/issues/9137"/>
</node>
</node>
<node TEXT="UI" ID="ID_162724784" CREATED="1395050907892" MODIFIED="1395050910607">
<node TEXT="Title" ID="ID_154064264" CREATED="1395050911924" MODIFIED="1395050914468">
<node TEXT="Get or Follow Link" ID="ID_1625093105" CREATED="1395050915410" MODIFIED="1395050919543"/>
</node>
<node TEXT="1) Get a link to:" ID="ID_1664988528" CREATED="1395050922810" MODIFIED="1395050945225"/>
<node TEXT="a) current editor" ID="ID_1337392249" CREATED="1395050945418" MODIFIED="1395050952518"/>
<node TEXT="text + button" ID="ID_1124484182" CREATED="1395050960378" MODIFIED="1395050972938">
<node TEXT="Copy to Clipboard" ID="ID_1514047833" CREATED="1395051056155" MODIFIED="1395051062288"/>
</node>
<node TEXT="b) all editors" ID="ID_1143884993" CREATED="1395050953241" MODIFIED="1395050957783"/>
<node TEXT="text + button" ID="ID_302798871" CREATED="1395050960378" MODIFIED="1395050972938">
<node TEXT="Copy to Clipboard" ID="ID_726385468" CREATED="1395051056155" MODIFIED="1395051062288"/>
</node>
<node TEXT="separator" ID="ID_1474009382" CREATED="1395050987717" MODIFIED="1395050990184"/>
<node TEXT="2) Or, paste link and follow it:" ID="ID_873949546" CREATED="1395050991703" MODIFIED="1395051009942"/>
<node TEXT="text + button" ID="ID_315930383" CREATED="1395051011227" MODIFIED="1395051014645">
<node TEXT="Follow" ID="ID_199949046" CREATED="1395051067193" MODIFIED="1395051069035"/>
</node>
</node>
<node TEXT="UI2" ID="ID_1126382881" CREATED="1395051083244" MODIFIED="1395051094010">
<node TEXT="Text boxes for get links (a, b): on focus =&gt; select all; read-only. On click =&gt; copy to clipboard and confirmation message" ID="ID_1639513019" CREATED="1395051095186" MODIFIED="1395051179383"/>
<node TEXT="Text box for follow link: accepts prefixed URL, or only query string" ID="ID_181607298" CREATED="1395051200767" MODIFIED="1395051220581"/>
</node>
</node>
<node TEXT="Debug menu" STYLE_REF="Done (Blue)" ID="ID_1509852301" CREATED="1396527984147" MODIFIED="1397039810675">
<node TEXT="Open Root" STYLE_REF="Done (Blue)" ID="ID_1879413757" CREATED="1396528018954" MODIFIED="1397039811435">
<node TEXT="GH198 - Move &quot;open...&quot; in debug menu" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_596199632" CREATED="1396528103289" MODIFIED="1396528195217" LINK="https://github.com/flower-platform/flower-platform-4/issues/198">
<node TEXT="Redmine" ID="ID_1875964310" CREATED="1396882945883" MODIFIED="1396882945913">
<node TEXT="RM9284 - GH198 - Move &quot;open...&quot; in debug menu" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_330313499" CREATED="1396882945913" MODIFIED="1396882945943" LINK="http://csp1/redmine/issues/9284"/>
<node TEXT="RM9285 - [Review] GH198 - Move &quot;open...&quot; in debug menu" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1439406562" CREATED="1396882946303" MODIFIED="1396882946313" LINK="http://csp1/redmine/issues/9285"/>
</node>
<node TEXT="don&apos;t use for now &quot;isDebug&quot;" ID="ID_1550862749" CREATED="1396528134778" MODIFIED="1396528148215"/>
<node TEXT="Open node =&gt; popup with input =&gt; open" ID="ID_1602997208" CREATED="1396528148561" MODIFIED="1396528185679"/>
<node TEXT="On load =&gt; open root" ID="ID_254740004" CREATED="1396528212984" MODIFIED="1396528217694"/>
</node>
</node>
</node>
</node>
<node TEXT="flexutil" STYLE_REF="Validated (Green)" POSITION="right" ID="ID_994293664" CREATED="1393250339599" MODIFIED="1394708368927">
<node TEXT="Controller and Type Descriptor" STYLE_REF="Validated (Green)" ID="ID_766085725" CREATED="1393250362089" MODIFIED="1394708364065"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      Similar system as in Java. Meant to be used for FlexDiagram.
    </p>
  </body>
</html>
</richcontent>
<node TEXT="GH79 - Controller and Type Descriptor and migrate FlexDiagram on this new system" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1888193330" CREATED="1393250362089" MODIFIED="1394708376572" LINK="https://github.com/flower-platform/flower-platform-4/issues/79">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_150185118" CREATED="1393250403050" MODIFIED="1393250403050">
<node TEXT="RM9047 - GH79 - Controller and Type Descriptor and migrate FlexDiagram on this new system" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_158145419" CREATED="1393250403050" MODIFIED="1393250403060" LINK="http://csp1/redmine/issues/9047"/>
<node TEXT="RM9048 - [Review] GH79 - Controller and Type Descriptor and migrate FlexDiagram on this new system" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_84590494" CREATED="1393250403580" MODIFIED="1393250403590" LINK="http://csp1/redmine/issues/9048"/>
</node>
</node>
</node>
<node TEXT="Actions" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_944199564" CREATED="1396008830105" MODIFIED="1397649917374">
<node TEXT="ComposedAction with single entry =&gt; delegate to it" STYLE_REF="Validated (Green)" ID="ID_281461547" CREATED="1396008833565" MODIFIED="1397649917381">
<node TEXT="Flag: delegateIfSingleChild" ID="ID_94779184" CREATED="1396008862845" MODIFIED="1396008908347">
<node TEXT="If true =&gt;" ID="ID_533870657" CREATED="1396008957775" MODIFIED="1396008961087"/>
<node TEXT="actAsNormalAction = true" ID="ID_191814774" CREATED="1396008961425" MODIFIED="1396008969487"/>
<node TEXT="label = self_label + singleChild.label" ID="ID_1372674779" CREATED="1396008969875" MODIFIED="1396009006244"/>
<node TEXT="on run =&gt; delegate to single child" ID="ID_1333970500" CREATED="1396009008212" MODIFIED="1396009019354"/>
</node>
<node TEXT="GH141 - New action with a single element: executable (because of settings)" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_352804747" CREATED="1392794810745" MODIFIED="1397649878116" LINK="https://github.com/flower-platform/flower-platform-4/issues/141">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_593154125" CREATED="1396008922157" MODIFIED="1396008922157">
<node TEXT="RM9226 - GH141 - New action with a single element: executable (because of settings)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_26237084" CREATED="1396008922157" MODIFIED="1396008922177" LINK="http://csp1/redmine/issues/9226"/>
<node TEXT="RM9227 - [Review] GH141 - New action with a single element: executable (because of settings)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_802136948" CREATED="1396008922737" MODIFIED="1396008922747" LINK="http://csp1/redmine/issues/9227"/>
</node>
</node>
</node>
</node>
<node TEXT="KeyBindings / Shortcuts" STYLE_REF="In progress (Yellow)" ID="ID_1476886180" CREATED="1397552264540" MODIFIED="1397635796020">
<node TEXT="GH239 - Improve existing Shortcut mechanism" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_277472211" CREATED="1397552278240" MODIFIED="1397657411043" LINK="https://github.com/flower-platform/flower-platform-4/issues/239">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_1063600469" STARTINCLINATION="783;0;" ENDINCLINATION="487;-185;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
<node TEXT="Redmine" ID="ID_991630786" CREATED="1397635803874" MODIFIED="1397635803897">
<node TEXT="RM9329 - GH239 - Improve existing Shortcut mechanism" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1981868845" CREATED="1397635803899" MODIFIED="1397635803924" LINK="http://csp1/redmine/issues/9329"/>
<node TEXT="RM9330 - [Review] GH239 - Improve existing Shortcut mechanism" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1302238555" CREATED="1397635804248" MODIFIED="1397635804257" LINK="http://csp1/redmine/issues/9330"/>
</node>
</node>
<node TEXT="Interactive configuration like in FreePlane" STYLE_REF="In progress (Yellow)" ID="ID_1933639868" CREATED="1397552376830" MODIFIED="1399022389717">
<node TEXT="GH240 - Interactive configuration like in FreePlane" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1571542231" CREATED="1397552376830" MODIFIED="1397552560530" LINK="https://github.com/flower-platform/flower-platform-4/issues/240">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_1063600469" STARTINCLINATION="791;0;" ENDINCLINATION="791;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
<node TEXT="Redmine" ID="ID_495522419" CREATED="1399022404768" MODIFIED="1399022404774">
<node TEXT="RM9373 - GH240 - Interactive configuration like in FreePlane" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1186697945" CREATED="1399022404775" MODIFIED="1399022404827" LINK="http://csp1/redmine/issues/9373"/>
<node TEXT="RM9374 - [Review] GH240 - Interactive configuration like in FreePlane" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_140029131" CREATED="1399022405160" MODIFIED="1399022405168" LINK="http://csp1/redmine/issues/9374"/>
</node>
</node>
</node>
</node>
<node TEXT="GH136 - Refactor: rename preStart" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_732755543" CREATED="1392986724252" MODIFIED="1395137290190" LINK="https://github.com/flower-platform/flower-platform-4/issues/136">
<node TEXT="preStart: mai toate aici; ii spunem startAndRegisterExtensions" ID="ID_1397253488" CREATED="1392986734663" MODIFIED="1392986782254"/>
<node TEXT="start: postStartAndProcessRegisteredExtensions" ID="ID_393694311" CREATED="1392986782862" MODIFIED="1392986804964"/>
</node>
</node>
<node TEXT="flexdiagram" STYLE_REF="In progress (Yellow)" POSITION="right" ID="ID_992825201" CREATED="1393344827052" MODIFIED="1397552179212">
<node TEXT="GH81 - Infinite loop when opening more than 2 mindmaps" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_1823293032" CREATED="1393344831066" MODIFIED="1394709144965" LINK="https://github.com/flower-platform/flower-platform-4/issues/81">
<icon BUILTIN="flag-green"/>
<node TEXT="Redmine" ID="ID_1473440777" CREATED="1393414034101" MODIFIED="1393414034131">
<node TEXT="RM9055 - GH81 - Infinite loop when opening more than 2 mindmaps" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1531242271" CREATED="1393414034131" MODIFIED="1393414034171" LINK="http://csp1/redmine/issues/9055"/>
<node TEXT="RM9056 - [Review] GH81 - Infinite loop when opening more than 2 mindmaps" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_155037072" CREATED="1393414034701" MODIFIED="1393414034711" LINK="http://csp1/redmine/issues/9056"/>
</node>
</node>
<node TEXT="GH144 - BUG: F2, ESC: F2 doesn&apos;t work again" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1050220515" CREATED="1395140380184" MODIFIED="1395140421930" LINK="https://github.com/flower-platform/flower-platform-4/issues/144">
<node TEXT="Redmine" ID="ID_1785818564" CREATED="1398671320813" MODIFIED="1398671320838">
<node TEXT="RM9354 - GH144 - BUG: F2, ESC: F2 doesn&apos;t work again" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_707391511" CREATED="1398671320839" MODIFIED="1398671320864" LINK="http://csp1/redmine/issues/9354"/>
<node TEXT="RM9355 - [Review] GH144 - BUG: F2, ESC: F2 doesn&apos;t work again" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1898840496" CREATED="1398671321472" MODIFIED="1398671321480" LINK="http://csp1/redmine/issues/9355"/>
</node>
<node TEXT="de fapt acum vad ca nu mai merge de loc dand:" ID="ID_1920811136" CREATED="1397649574522" MODIFIED="1397649584265"/>
<node TEXT="TypeError: Error #1009: Cannot access a property or method of a null object reference.&#xa;at org.flowerplatform.flexdiagram.tool::Tool/getRendererFromDisplayCoordinates()[D:\data\java_work\git_repo\flower-platform-4\org.flowerplatform.flexdiagram\src\org\flowerplatform\flexdiagram\tool\Tool.as:81]&#xa;at org.flowerplatform.flexdiagram.tool::InplaceEditorTool/keyDownHandler()[D:\data\java_work\git_repo\flower-platform-4\org.flowerplatform.flexdiagram\src\org\flowerplatform\flexdiagram\tool\InplaceEditorTool.as:122]" ID="ID_138538786" CREATED="1397649616550" MODIFIED="1397649618338"/>
</node>
<node TEXT="GH203 - IED -&gt; auto grow issue" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_590667381" CREATED="1396505825824" MODIFIED="1396528574681" LINK="https://github.com/flower-platform/flower-platform-4/issues/203">
<node TEXT="Redmine" ID="ID_1690638855" CREATED="1396866771925" MODIFIED="1396866771932">
<node TEXT="RM9278 - GH203 - IED -&gt; auto grow issue" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_949661143" CREATED="1396866771935" MODIFIED="1396866771983" LINK="http://csp1/redmine/issues/9278"/>
<node TEXT="RM9279 - [Review] GH203 - IED -&gt; auto grow issue" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1675372386" CREATED="1396866772304" MODIFIED="1396866772313" LINK="http://csp1/redmine/issues/9279"/>
</node>
</node>
<node TEXT="MindMap" STYLE_REF="Done (Blue)" FOLDED="true" ID="ID_1835563309" CREATED="1397552106370" MODIFIED="1397635790812">
<node TEXT="Show/hide root node" STYLE_REF="Validated (Green)" FOLDED="true" ID="ID_1985721797" CREATED="1394116472105" MODIFIED="1395235437432">
<node TEXT="GH108 - Show/hide root node" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_819512228" CREATED="1394116472105" MODIFIED="1394116512918" LINK="https://github.com/flower-platform/flower-platform-4/issues/108">
<node TEXT="Redmine" ID="ID_1979347528" CREATED="1394791457356" MODIFIED="1394791457358">
<node TEXT="RM9129 - GH108 - Show/hide root node" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_682706361" CREATED="1394791457359" MODIFIED="1394791457378" LINK="http://csp1/redmine/issues/9129"/>
<node TEXT="RM9130 - [Review] GH108 - Show/hide root node" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_249591427" CREATED="1394791457896" MODIFIED="1394791457902" LINK="http://csp1/redmine/issues/9130"/>
</node>
</node>
</node>
<node TEXT="Navigation Actions" STYLE_REF="Done (Blue)" ID="ID_345926065" CREATED="1397552128260" MODIFIED="1397635789860">
<node TEXT="Up/Down" ID="ID_855802251" CREATED="1397552137830" MODIFIED="1397552143772"/>
<node TEXT="PgUp/PgDown" ID="ID_1660974573" CREATED="1397552161850" MODIFIED="1397552166812"/>
<node TEXT="Left/Right" ID="ID_1986641889" CREATED="1397552143990" MODIFIED="1397552146372"/>
<node TEXT="GH238 - Navigation Actions" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_157855042" CREATED="1397546344576" MODIFIED="1397546355017" LINK="https://github.com/flower-platform/flower-platform-4/issues/238">
<node TEXT="Redmine" ID="ID_391073813" CREATED="1397546365897" MODIFIED="1397546365897">
<node TEXT="RM9322 - GH238 - Navigation Actions" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_426577281" CREATED="1397546365897" MODIFIED="1397546365907" LINK="http://csp1/redmine/issues/9322"/>
<node TEXT="RM9323 - [Review] GH238 - Navigation Actions" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_2793740" CREATED="1397546366417" MODIFIED="1397546366427" LINK="http://csp1/redmine/issues/9323"/>
</node>
</node>
</node>
</node>
<node TEXT="GH244 - BUG: Pan issue on mobile; seems to be event handling related" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1918601157" CREATED="1397648889038" MODIFIED="1397648912085" LINK="https://github.com/flower-platform/flower-platform-4/issues/244">
<node TEXT="Redmine" ID="ID_886084391" CREATED="1398687309854" MODIFIED="1398687309880">
<node TEXT="RM9356 - GH244 - BUG: Pan issue on mobile; seems to be event handling related" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_755250342" CREATED="1398687309881" MODIFIED="1398687309907" LINK="http://csp1/redmine/issues/9356"/>
<node TEXT="RM9357 - [Review] GH244 - BUG: Pan issue on mobile; seems to be event handling related" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_822845985" CREATED="1398687310447" MODIFIED="1398687310456" LINK="http://csp1/redmine/issues/9357"/>
</node>
</node>
</node>
<node TEXT="freeplane" POSITION="right" ID="ID_1813661012" CREATED="1391777791745" MODIFIED="1391777794376">
<node TEXT="Persistence related controllers" STYLE_REF="Done (Blue)" ID="ID_526112370" CREATED="1391777797235" MODIFIED="1393250096946" LINK="#ID_1057628910"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      Controllers (children/props provider, property setter, etc) registered for the &quot;freeplane&quot; resource type.
    </p>
  </body>
</html>
</richcontent>
</node>
<node TEXT="MindMap flow related controllers" STYLE_REF="New (Red)" ID="ID_1033677364" CREATED="1391777866134" MODIFIED="1391778019666"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      Controllers that deal with the flow specific to mindmaps. E.g. &quot;special&quot; properties for node color, font, size, etc
    </p>
  </body>
</html>
</richcontent>
</node>
<node TEXT="GH114 - Create new MM file from UI" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1387884937" CREATED="1394460738863" MODIFIED="1394460754784" LINK="https://github.com/flower-platform/flower-platform-4/issues/114">
<node TEXT="Redmine" ID="ID_123776292" CREATED="1395997602648" MODIFIED="1395997602667">
<node TEXT="RM9224 - GH114 - Create new MM file from UI" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1642981905" CREATED="1395997602669" MODIFIED="1395997602699" LINK="http://csp1/redmine/issues/9224"/>
<node TEXT="RM9225 - [Review] GH114 - Create new MM file from UI" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1257698365" CREATED="1395997603019" MODIFIED="1395997603025" LINK="http://csp1/redmine/issues/9225"/>
</node>
</node>
<node TEXT="GH115 - Process all properties to be able to change storage layer: MM -&gt; server -&gt; MM" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1671313850" CREATED="1394460774824" MODIFIED="1394460854918" LINK="https://github.com/flower-platform/flower-platform-4/issues/115">
<node TEXT="Am impresia ca el stie sa deschida versiuni mai noi, sa salveze si apoi sa scrie cu info pe care nu le-a putut interpreta" ID="ID_824521982" CREATED="1395406386542" MODIFIED="1395406415297"/>
<node TEXT="Am impresia ca FreeMind poate deschide Freeplane, salva, si apoi in Freeplane in continuare se vede cum trebuie" ID="ID_1012723529" CREATED="1395406415566" MODIFIED="1395406443512"/>
<node TEXT="de vazut" ID="ID_506680480" CREATED="1395406443782" MODIFIED="1395406445173"/>
</node>
<node TEXT="GH157 - Problems when working with files with spaces" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_874690946" CREATED="1394794942776" MODIFIED="1395233267428" LINK="https://github.com/flower-platform/flower-platform-4/issues/157">
<node TEXT="Also in file system nodes?" ID="ID_1537182792" CREATED="1395233251634" MODIFIED="1395233262908"/>
<node TEXT="Redmine" ID="ID_1073650097" CREATED="1395665327919" MODIFIED="1395665327949">
<node TEXT="RM9191 - GH157 - Problems when working with files with spaces" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_982822573" CREATED="1395665327949" MODIFIED="1395665327979" LINK="http://csp1/redmine/issues/9191"/>
<node TEXT="RM9192 - [Review] GH157 - Problems when working with files with spaces" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_35233734" CREATED="1395665328509" MODIFIED="1395665328519" LINK="http://csp1/redmine/issues/9192"/>
</node>
<node TEXT="Construction site" FOLDED="true" ID="ID_955572867" CREATED="1389360401598" MODIFIED="1399300538104">
<icon BUILTIN="mindmap"/>
<node TEXT="Issue" ID="ID_1322454893" CREATED="1397040855094" MODIFIED="1397040857205">
<node TEXT="Exception thrown when open editor with encoded chars" ID="ID_1248020083" CREATED="1396939541740" MODIFIED="1397040882545">
<node TEXT="INFO: [BlazeDS Flower Web][Message.Remoting] Error processing remote invocation: java.lang.RuntimeException: java.nio.file.NoSuchFileException: C:\Users\PowerUser\git\flower-platform-4\org.flowerplatform.host.web_app\workspace\repository\test\FAP%20-%20Flower%20Platform%204.mm&#xa;  incomingMessage: Flex Message (flex.messaging.messages.RemotingMessage)&#xa;    operation = getNode&#xa;    clientId = 921A7BEA-20D0-71B5-3E00-8A17130FEF0F&#xa;    destination = nodeService&#xa;    messageId = E8A0E29A-974D-E53A-2812-3CACB5DB8E65&#xa;    timestamp = 1396882322917&#xa;    timeToLive = 0&#xa;    body =&#xa;    [&#xa;      (fileNode|(fileSystem|self|)|test/FAP%20-%20Flower%20Platform%204.mm)&#xa;    ]&#xa;    hdr(DSId) = 921A777D-50A6-47B4-B4FC-46A917E0389F&#xa;    hdr(DSEndpoint) = remoting-amf&#xa;java.lang.RuntimeException: java.nio.file.NoSuchFileException: C:\Users\PowerUser\git\flower-platform-4\org.flowerplatform.host.web_app\workspace\repository\test\FAP%20-%20Flower%20Platform%204.mm&#xa;at org.flowerplatform.core.file.FilePropertiesProvider.populateWithProperties(FilePropertiesProvider.java:48)&#xa;at org.flowerplatform.core.node.NodeService.populateNodeProperties(NodeService.java:287)&#xa;at org.flowerplatform.core.node.remote.Node.getOrPopulateProperties(Node.java:125)&#xa;at org.flowerplatform.core.node.remote.NodeServiceRemote.getNode(NodeServiceRemote.java:100)&#xa;at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)&#xa;Caused by: java.nio.file.NoSuchFileException: C:\Users\PowerUser\git\flower-platform-4\org.flowerplatform.host.web_app\workspace\repository\test\FAP%20-%20Flower%20Platform%204.mm&#xa;at sun.nio.fs.WindowsException.translateToIOException(WindowsException.java:79)&#xa;at sun.nio.fs.WindowsException.rethrowAsIOException(WindowsException.java:97)&#xa;at sun.nio.fs.WindowsException.rethrowAsIOException(WindowsException.java:102)&#xa;at sun.nio.fs.WindowsFileAttributeViews$Basic.readAttributes(WindowsFileAttributeViews.java:53)&#xa;at sun.nio.fs.WindowsFileAttributeViews$Basic.readAttributes(WindowsFileAttributeViews.java:38)&#xa;at sun.nio.fs.AbstractBasicFileAttributeView.readAttributes(AbstractBasicFileAttributeView.java:168)&#xa;at sun.nio.fs.AbstractFileSystemProvider.readAttributes(AbstractFileSystemProvider.java:92)&#xa;at java.nio.file.Files.readAttributes(Files.java:1902)&#xa;at org.flowerplatform.core.file.FilePropertiesProvider.populateWithProperties(FilePropertiesProvider.java:46)&#xa;... 44 more" ID="ID_1527504818" CREATED="1396939534548" MODIFIED="1396939535905"/>
</node>
</node>
</node>
</node>
<node TEXT="GH262 - BUG: Problems when opening freeplane help mm file (freeplane.mm)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_135973665" CREATED="1398864672649" MODIFIED="1399300520542" LINK="https://github.com/flower-platform/flower-platform-4/issues/262">
<node TEXT="Redmine" ID="ID_106572735" CREATED="1399300544265" MODIFIED="1399300544267">
<node TEXT="RM9387 - GH262 - BUG: Problems when opening freeplane help mm file (freeplane.mm)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_403573074" CREATED="1399300544268" MODIFIED="1399300544273" LINK="http://csp1/redmine/issues/9387"/>
<node TEXT="RM9388 - [Review] GH262 - BUG: Problems when opening freeplane help mm file (freeplane.mm)" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1758425201" CREATED="1399300544599" MODIFIED="1399300544608" LINK="http://csp1/redmine/issues/9388"/>
</node>
</node>
</node>
<node TEXT="host.idea" POSITION="right" ID="ID_302783912" CREATED="1390480264308" MODIFIED="1390480280442"/>
<node TEXT="host.web_app" STYLE_REF="Validated (Green)" FOLDED="true" POSITION="right" ID="ID_1008364373" CREATED="1392715720942" MODIFIED="1392715783433">
<icon BUILTIN="messagebox_warning"/>
<node TEXT="Limitations" STYLE_REF="New (Red)" ID="ID_73730574" CREATED="1392636667931" MODIFIED="1393929633739">
<icon BUILTIN="messagebox_warning"/>
<font BOLD="true"/>
<node TEXT="When session serialization is enabled (which is default), we have exception when the server restarts" FOLDED="true" ID="ID_438652248" CREATED="1392717007017" MODIFIED="1392717037138">
<node TEXT="By registering HttpFlexSession as a listener (to avoid duplicate session error after session expiration), BlazeDS adds the flex session as an attribute for the session. By default, tomcat persists active sessions across restarts; this means that on restart, it will attempt to deserialize the persisted sessions with their attributes, and will throw an exception because the class HttpFlexSession cannot be found, and the sessions will not be deserialized.&#xa;&#xa;To avoid this, we disabled sessions persistence across server restarts (as explained by http://mihail.stoynov.com/2011/04/11/disable-session-persistence-in-tomcat/). This way, sessions will not be saved and restored after restart (which is not a bad thing, since we don&apos;t keep any info in the session for the moment); furthermore, it seems that active sessions will be destroyed on server stop." ID="ID_851087515" CREATED="1392716054536" MODIFIED="1392716544565"/>
</node>
</node>
</node>
<node TEXT="Management" POSITION="left" ID="ID_1129796123" CREATED="1389389265204" MODIFIED="1389390185389">
<icon BUILTIN="executable"/>
<node TEXT="To discuss next meeting" ID="ID_1665764600" CREATED="1396428487726" MODIFIED="1396428506517">
<icon BUILTIN="group"/>
<font BOLD="true"/>
<node TEXT="TODO this release (Mind Map prototype)" ID="ID_1939597264" CREATED="1396430523387" MODIFIED="1396430548438"/>
<node TEXT="TODO next release (Mind Map usable/stable)" ID="ID_1899107790" CREATED="1396430530626" MODIFIED="1396430565748"/>
<node TEXT="CC" ID="ID_1792776220" CREATED="1396505819290" MODIFIED="1396505821197">
<node TEXT="tests" STYLE_REF="Validated (Green)" ID="ID_237558225" CREATED="1396505587131" MODIFIED="1396528487733"/>
<node TEXT="users management, login with facebook, google+ etc." ID="ID_945448044" CREATED="1396505841888" MODIFIED="1396505910748"/>
<node TEXT="Show Root action must be replaced,  Show Editor removed" STYLE_REF="Validated (Green)" ID="ID_1728061361" CREATED="1396506494539" MODIFIED="1396528206381"/>
<node TEXT="mobile" ID="ID_453837806" CREATED="1396506688529" MODIFIED="1396506725870">
<node TEXT="intro page" ID="ID_1678939343" CREATED="1396506726760" MODIFIED="1396506728797"/>
<node TEXT="check flow &amp; all views display" ID="ID_1578129704" CREATED="1396506730407" MODIFIED="1396506815861"/>
<node TEXT="some issues with users accounts feature" ID="ID_218293581" CREATED="1396506745161" MODIFIED="1396939469544"/>
</node>
<node TEXT="font size -&gt; make real size bigger only when displaying" STYLE_REF="Validated (Green)" ID="ID_1572365163" CREATED="1396506126477" MODIFIED="1396529296413"/>
<node TEXT="logo flower + icon for browser" STYLE_REF="Validated (Green)" ID="ID_1732905185" CREATED="1396506667553" MODIFIED="1396529119629"/>
<node TEXT="Testing Session" ID="ID_128769365" CREATED="1396939488668" MODIFIED="1396939504394">
<node TEXT="bugs" ID="ID_1151068637" CREATED="1396939509733" MODIFIED="1396939511618">
<node TEXT="fileSystem - &gt; operations (add/remove) on first level of file nodes aren&apos;t displayed on client side (probably no updates are sent) #202" STYLE_REF="Done (Blue)" ID="ID_1927270166" CREATED="1396939697234" MODIFIED="1397031332317">
<hook NAME="FirstGroupNode"/>
</node>
<node TEXT="icons from sidebar must be added only for a mindmap node #202" STYLE_REF="Done (Blue)" ID="ID_1400861672" CREATED="1396939815586" MODIFIED="1397033026861"/>
<node TEXT="recycling issue -&gt; node positions aren&apos;t correctly calculated #226" STYLE_REF="Done (Blue)" ID="ID_1824286577" CREATED="1396939827754" MODIFIED="1397129927897"/>
<node TEXT="remove btns from icons view doesn&apos;t work anymore #202" STYLE_REF="Done (Blue)" ID="ID_136315087" CREATED="1396939869945" MODIFIED="1397035861479"/>
<node TEXT="refresh -&gt; exception thrown when executed twice on same selection #200" STYLE_REF="Done (Blue)" FOLDED="true" ID="ID_1670548753" CREATED="1396939897857" MODIFIED="1397053127558">
<node TEXT="TypeError: Error #1009: Cannot access a property or method of a null object reference.&#xa;at org.flowerplatform.flex_client.core.editor.action::DiagramShellAwareActionBase/get diagramShell()[C:\Users\PowerUser\git\flower-platform-4\org.flowerplatform.flex_client.core\src\org\flowerplatform\flex_client\core\editor\action\DiagramShellAwareActionBase.as:38]&#xa;at org.flowerplatform.flex_client.mindmap.action::RefreshAction/doRun()[C:\Users\PowerUser\git\flower-platform-4\org.flowerplatform.flex_client.mindmap\src\org\flowerplatform\flex_client\mindmap\action\RefreshAction.as:46]&#xa;at org.flowerplatform.flex_client.core.editor.action::DiagramShellAwareActionBase/run()[C:\Users\PowerUser\git\flower-platform-4\org.flowerplatform.flex_client.core\src\org\flowerplatform\flex_client\core\editor\action\DiagramShellAwareActionBase.as:57]&#xa;at org.flowerplatform.flexutil.context_menu::ContextMenuItemRenderer/clickHandler()[C:\Users\PowerUser\git\flower-platform-4\org.flowerplatform.flexutil\src\org\flowerplatform\flexutil\context_menu\ContextMenuItemRenderer.mxml:65]&#xa;at org.flowerplatform.flexutil.context_menu::ContextMenuItemRenderer/___ContextMenuItemRenderer_ItemRenderer1_click()[C:\Users\PowerUser\git\flower-platform-4\org.flowerplatform.flexutil\src\org\flowerplatform\flexutil\context_menu\ContextMenuItemRenderer.mxml:24]" ID="ID_1070047265" CREATED="1396939909337" MODIFIED="1396939910550"/>
</node>
<node TEXT="New File/Folder -&gt; no icon #202" STYLE_REF="Done (Blue)" ID="ID_784838469" CREATED="1396939950617" MODIFIED="1397036359293"/>
<node TEXT="on mobile -&gt; error when clicking on &quot;...&quot; button from Properties -&gt; Icons #202" STYLE_REF="Done (Blue)" ID="ID_1307275237" CREATED="1397033446313" MODIFIED="1397050859452"/>
</node>
<node TEXT="open mm with rootNode in center #200" STYLE_REF="Done (Blue)" ID="ID_1853294806" CREATED="1396939639819" MODIFIED="1397053127548"/>
<node TEXT="more user friendly tab label for editors" ID="ID_845546952" CREATED="1396939668116" MODIFIED="1396939686825"/>
<node TEXT="Properties -&gt; use text instead label to wrap text (if too long -&gt; view width too long) #202" STYLE_REF="Done (Blue)" ID="ID_74633927" CREATED="1396939848650" MODIFIED="1397050873144"/>
<node TEXT="New File/Folder &amp; Rename -&gt; focus on text at init #202" STYLE_REF="Done (Blue)" ID="ID_1975852570" CREATED="1396939919706" MODIFIED="1397050894120"/>
<node TEXT="improve recycle mechanism -&gt; works bad on mobile" ID="ID_593959271" CREATED="1396941395880" MODIFIED="1396941412098"/>
<node TEXT="on mobile -&gt; back button doesn&apos;t work; can we remove Exit btn?" ID="ID_1801772511" CREATED="1397033742692" MODIFIED="1397033863980"/>
<node TEXT="we need a colorPicker compatible with mobile" STYLE_REF="New (Red)" ID="ID_1191134467" CREATED="1397034008589" MODIFIED="1397040138536"/>
</node>
<node TEXT="BUG: Problems when opening freeplane help mm file (freeplane.mm)" STYLE_REF="Done (Blue)" ID="ID_29154627" CREATED="1398864672649" MODIFIED="1399462882066"/>
<node TEXT="after a node is added, it must be populated with properties (addChild)" STYLE_REF="Done (Blue)" ID="ID_1178797608" CREATED="1398864812140" MODIFIED="1399462880515"/>
<node TEXT="viewhost: acceptam inchiderea prin ESC, dar nu trece prin handler-ul de close al view-ului" ID="ID_1196556525" CREATED="1399297246965" MODIFIED="1399297294504"/>
</node>
<node TEXT="MG" ID="ID_1132472161" CREATED="1396515570676" MODIFIED="1396515572377">
<node TEXT="mindmap shortcuts for easy navigation" ID="ID_1882732177" CREATED="1396515574072" MODIFIED="1396515687219"/>
<node TEXT="on close editor: should we unsubsribe if it was the only client? it&apos;s very confusing right now" ID="ID_507445586" CREATED="1396515997111" MODIFIED="1396516027449"/>
<node TEXT="bugs" ID="ID_1763959585" CREATED="1396515688393" MODIFIED="1396515719075"/>
</node>
<node TEXT="SS" ID="ID_1360895116" CREATED="1396598257766" MODIFIED="1396598262742">
<node TEXT="create/edit styles" ID="ID_443719559" CREATED="1396598264422" MODIFIED="1396598321266"/>
<node TEXT="slect node style" STYLE_REF="Validated (Green)" ID="ID_356260725" CREATED="1396598324375" MODIFIED="1397053151739"/>
<node TEXT="bugs" ID="ID_1971547039" CREATED="1396598406394" MODIFIED="1396598410166">
<node TEXT="mobile .createMessageBox (setWidth setHeight dosn&apos;t work)" ID="ID_807389532" CREATED="1396607521701" MODIFIED="1396607669615"/>
<node TEXT="each time &apos;cloud shape&apos; property is changed, the space between nodes increases" STYLE_REF="Done (Blue)" ID="ID_1124566030" CREATED="1396607694072" MODIFIED="1397129962148">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_1824286577" STARTINCLINATION="450;0;" ENDINCLINATION="450;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
<node TEXT="new File/Folder when the parent is not a Dir, donsn&apos;t work anymore #202" STYLE_REF="Done (Blue)" ID="ID_1267601190" CREATED="1397462762780" MODIFIED="1397465177375"/>
<node TEXT="Utils.getCompatibleHTMLText,  adds some extra newline characters" ID="ID_1853696805" CREATED="1397642338503" MODIFIED="1397642664676"/>
<node TEXT="eclipse independent tests, don&apos;t work anymore" ID="ID_574190106" CREATED="1397642586977" MODIFIED="1397642633812"/>
<node TEXT="Problem when adding node on left side" STYLE_REF="Done (Blue)" ID="ID_1471459199" CREATED="1398431614539" MODIFIED="1399467366359"/>
</node>
</node>
</node>
<node TEXT="Idei" FOLDED="true" ID="ID_1479518601" CREATED="1390457120820" MODIFIED="1390458194432">
<icon BUILTIN="idea"/>
<node TEXT="Android: editare mm offline" ID="ID_779681201" CREATED="1390457127146" MODIFIED="1390457155399">
<node TEXT="impachetam serverul intr-ul apk cu openjdk &amp; co" ID="ID_1565572656" CREATED="1390457156388" MODIFIED="1390457169088"/>
</node>
<node TEXT="Integrare si cu alte soft-uri de mm; i.e. import, export" ID="ID_675946729" CREATED="1390457329595" MODIFIED="1390457342498"/>
<node TEXT="Ar merge sa facem aplicatia in cadrul Google Apps? De fapt este vorba de aplicatie care se conecteaza la Drive prin API. Si apoi cred ca merge marketata prin sistemul lor." ID="ID_336875364" CREATED="1396074033976" MODIFIED="1396074374043"/>
</node>
<node TEXT="Versiuni livrabile" FOLDED="true" ID="ID_1660416774" CREATED="1392200269856" MODIFIED="1392200277287">
<node TEXT="FreePlane Web intern" ID="ID_1422718094" CREATED="1392200284716" MODIFIED="1392200521687">
<node TEXT="1" OBJECT="java.lang.Long|1" ID="ID_1553724521" CREATED="1392217414100" MODIFIED="1392217416531">
<node TEXT="Afisare de file system" ID="ID_1427774345" CREATED="1392200522806" MODIFIED="1392200691267">
<node TEXT="Teoretic facut; de review" ID="ID_695317021" CREATED="1392200741607" MODIFIED="1392200756208"/>
<node TEXT="Afisam root/fs? Se configureaza repo si aici?" STYLE_REF="New (Red)" ID="ID_1484907781" CREATED="1393348030606" MODIFIED="1393348060437">
<icon BUILTIN="help"/>
</node>
</node>
<node TEXT="Create a FreePlane mindmap" ID="ID_996072348" CREATED="1392200760847" MODIFIED="1392200788938"/>
<node TEXT="Open a FreePlane mindmap" ID="ID_977835087" CREATED="1392200789207" MODIFIED="1392200803818"/>
<node TEXT="Dirty marker; save logic" ID="ID_154612662" CREATED="1392217474634" MODIFIED="1392217556343"/>
<node TEXT="Debug tree" ID="ID_63423820" CREATED="1392200806776" MODIFIED="1392200812409"/>
</node>
<node TEXT="2" OBJECT="java.lang.Long|2" ID="ID_136386494" CREATED="1392217424346" MODIFIED="1392217424554">
<node TEXT="User login, management" ID="ID_1425615147" CREATED="1392217429014" MODIFIED="1392721522371" COLOR="#000000"/>
<node TEXT="Mobile" ID="ID_1838452256" CREATED="1392217559363" MODIFIED="1392217562283"/>
</node>
</node>
<node TEXT="CodeSync Eclipse intern" ID="ID_1635191051" CREATED="1392217386607" MODIFIED="1392217398292">
<node TEXT="1" OBJECT="java.lang.Long|1" ID="ID_1729816073" CREATED="1392222464964" MODIFIED="1392222465779">
<node TEXT="Gestiune de repo (din tree)" ID="ID_1520061923" CREATED="1393348107509" MODIFIED="1393348213010">
<node TEXT="cu nod de fisiere imbricat" ID="ID_1998103585" CREATED="1393348147413" MODIFIED="1393348155235"/>
<node TEXT="deschidere de 2 resurse" ID="ID_307584003" CREATED="1393348160232" MODIFIED="1393348165082"/>
</node>
</node>
</node>
<node TEXT="CodeSync server/agregare" ID="ID_746642535" CREATED="1393348398023" MODIFIED="1393348410528">
<node TEXT="1" OBJECT="java.lang.Long|1" ID="ID_1351998989" CREATED="1393348411480" MODIFIED="1393348412701"/>
</node>
</node>
<node TEXT="TODO" ID="ID_1784165884" CREATED="1392217118370" MODIFIED="1392217121167">
<node TEXT="Pentru CodeSync&#xa;(in viitor, cand il bagam in prod)" FOLDED="true" ID="ID_21942407" CREATED="1392212959506" MODIFIED="1392212978268">
<node TEXT="Conversie intre feature care de fapt sunt containemnt, dar le prezentam ca valoare; e.g. modif, visib, isStatic, etc." ID="ID_951329512" CREATED="1391792611618" MODIFIED="1391792653380"/>
<node TEXT="Modify CodeSync algorithm to process all children at once; i.e. no need to break down by containment feature type" ID="ID_1597974288" CREATED="1392213027762" MODIFIED="1392213076880">
<node TEXT="La momentul de fata asta e o pierdere de performanta. Pt ca pe model sunt la gramada, necategorisiti (cum erau la un mom dat). Deci de fiecare data cand se dau acesti copii, se parcurge lista de mai multe ori." ID="ID_198044630" CREATED="1392213077578" MODIFIED="1392213125789"/>
<node TEXT="Ar fi interesant, ca pt cazul RegEx avem ac. logica; deci acolo am face poate de 2 ori procesarea asta?" ID="ID_1109710464" CREATED="1392213127007" MODIFIED="1392213145913"/>
<node TEXT="In java era interesant, caci AST-ul deja era pe aceste categorii" ID="ID_1158006090" CREATED="1392213167253" MODIFIED="1392213182305"/>
</node>
<node TEXT="Reuse definitions of model adapters/feature providers (from alg) &lt;-&gt; AddChildProvider, PropertyProvider, etc" ID="ID_433004760" CREATED="1392213202913" MODIFIED="1392213285361">
<node TEXT="Ceva reutilizare se poate face" ID="ID_49590597" CREATED="1392213286229" MODIFIED="1392213294553"/>
<node TEXT="Insa nu avem corespondenta 1 la 1. E.g. datorita acelor &quot;feature virtuale&quot;, e.g. isVisible, isAbstract care se traduc intr-o lista (modifiers)" ID="ID_1148516837" CREATED="1392213294781" MODIFIED="1392213340057"/>
</node>
<node TEXT="Display packages in a compact form (e.g. com.crispico.myPackage)" ID="ID_733742416" CREATED="1392213545328" MODIFIED="1392213560762"/>
<node TEXT="Lock on repository level during codeSync" ID="ID_258352200" CREATED="1392303633540" MODIFIED="1392303644532"/>
<node TEXT="2 step codeSync, with intermediate screen showing the match tree" ID="ID_1076966404" CREATED="1392303644961" MODIFIED="1392303674592"/>
<node TEXT="Annotations/match keys &amp; co" ID="ID_1509284249" CREATED="1392813844903" MODIFIED="1392813855704">
<node TEXT="De tratat annotarile. In cazul in care se adauga o valoare noua pt o anotare din model, daca anotarea era marker sau single value, trebuie de fapt inlocuita cu un NormalAnnotation; dar daca fac asta, o sa am probleme in timpul sync, pt ca orice modificare va lucra in continure pe anotarea stearsa. Pt mai tarziu: din java 8, o sa pot sa am aceeasi anotare de mai multe ori =&gt; match key nu o sa fie suficient sa fie calculat in functie de nume. In plus, se vor putea pune anotari oriunde, nu doar pe declaratii ca acum. Asta nu o sa putem detecta decat dupa ce JDT va face update pentru java 8 (momentan sunt in beta)." ID="ID_768710022" CREATED="1392813856604" MODIFIED="1392813856604"/>
<node TEXT="deocamdata nu merge un test din cauza asta; (e dezactivat)" ID="ID_1834776881" CREATED="1394542044028" MODIFIED="1394542059038"/>
</node>
<node TEXT="Automatic rename class &lt;-&gt; file (compilation unit)" ID="ID_1134166888" CREATED="1394543250470" MODIFIED="1394543271201"/>
</node>
<node TEXT="General" ID="ID_949225202" CREATED="1392213570560" MODIFIED="1392213978875">
<node TEXT="Low" ID="ID_1030481928" CREATED="1392213980153" MODIFIED="1392213987645">
<node TEXT="Group children per childrenCategory" ID="ID_1905850002" CREATED="1392213575880" MODIFIED="1392213599344">
<node TEXT="inclusiv actiunile de creere, grupate pe submeniu" ID="ID_362170523" CREATED="1392213600332" MODIFIED="1392213617896"/>
</node>
</node>
<node TEXT="Medium" ID="ID_757082171" CREATED="1392214151569" MODIFIED="1392214154531"/>
<node TEXT="High" ID="ID_1356795684" CREATED="1392213991883" MODIFIED="1392213993805">
<node TEXT="Sincronize &quot;codesync&quot; resources with &quot;codesync config&quot;" ID="ID_456580949" CREATED="1394039676026" MODIFIED="1394039692027"/>
<node TEXT="Users" ID="ID_889207153" CREATED="1392980893914" MODIFIED="1392980895635"/>
<node TEXT="Mobile app" ID="ID_718986418" CREATED="1392980895863" MODIFIED="1392980898375"/>
<node TEXT="Repo Management UI/Dashboard Screen" FOLDED="true" ID="ID_299042749" CREATED="1393273321007" MODIFIED="1393273342168">
<node TEXT="Components" ID="ID_248754487" CREATED="1390992734668" MODIFIED="1390992740980">
<node TEXT="List of Repositories" ID="ID_937669616" CREATED="1390941618899" MODIFIED="1390941625561"/>
<node TEXT="List of Projects&#xa;(cf. selected repo)" ID="ID_518158622" CREATED="1390941629559" MODIFIED="1390941645741"/>
<node TEXT="List of Src Dirs &amp; technology&#xa;(cf. selected project)" ID="ID_715375804" CREATED="1390992707348" MODIFIED="1390992729160"/>
<node TEXT="Items are links. On click =&gt; the corresponding MM is opened, and the node is selected" ID="ID_833079904" CREATED="1390993243021" MODIFIED="1390993262653"/>
</node>
<node TEXT="Actions" ID="ID_1990361498" CREATED="1390992748418" MODIFIED="1390992749910">
<node TEXT="Repos" ID="ID_287857512" CREATED="1390993000190" MODIFIED="1390993004442">
<node TEXT="New Flower Repository Project&#xa;(SWT, invokable also via normal &quot;new/project&quot; menu)" ID="ID_590613501" CREATED="1390942256940" MODIFIED="1390992849742">
<node TEXT="Dialog with location" ID="ID_510142961" CREATED="1390942297210" MODIFIED="1390942317813"/>
<node TEXT="Some explanation: &quot;should sit next to your projects&quot;" ID="ID_1286743764" CREATED="1390942318241" MODIFIED="1390942332683"/>
<node TEXT="Opens the Dashboard" ID="ID_1415865284" CREATED="1390942376081" MODIFIED="1390942386543"/>
</node>
<node TEXT="Remove Flower Repository Project" ID="ID_492646966" CREATED="1390992861440" MODIFIED="1390992874502">
<node TEXT="invokes the standard remove project logic (SWT)" ID="ID_852689214" CREATED="1390992885570" MODIFIED="1390992910042"/>
</node>
<node TEXT="Import ...." ID="ID_1240120977" CREATED="1390992902750" MODIFIED="1390992949292">
<node TEXT="invokes the standard import project; maybe with an additional check, to make sure it&apos;s a FP Repo Project" ID="ID_900179278" CREATED="1390992950360" MODIFIED="1390992977362"/>
</node>
</node>
<node TEXT="Projects" ID="ID_991574531" CREATED="1390993015270" MODIFIED="1390993017912">
<node TEXT="Add project" ID="ID_729039201" CREATED="1390993106570" MODIFIED="1390993110222">
<node TEXT="From an existing Eclipse project" ID="ID_62647866" CREATED="1390993111280" MODIFIED="1390993119792"/>
</node>
<node TEXT="Remove project" ID="ID_853700658" CREATED="1390993123480" MODIFIED="1390993126962"/>
</node>
<node TEXT="SrcDirs" ID="ID_1579922481" CREATED="1390993140580" MODIFIED="1390993142732">
<node TEXT="Add Edit SrcDirs" ID="ID_350607722" CREATED="1390993143711" MODIFIED="1390993173353">
<node TEXT="Checkbox tree" ID="ID_1155691577" CREATED="1390993154511" MODIFIED="1390993180403"/>
<node TEXT="for selected node, we have a checkbox list with technologies" ID="ID_297697143" CREATED="1390993180721" MODIFIED="1390993207813"/>
<node TEXT="Some aid: detect src dirs (at least for java)" ID="ID_1544346998" CREATED="1391002569835" MODIFIED="1391002599407"/>
</node>
</node>
</node>
<node TEXT="Project structure example" ID="ID_638135104" CREATED="1393273465454" MODIFIED="1393273473709">
<node TEXT="flower-platform-repo.flower-platform-4" ID="ID_806949079" CREATED="1391021095649" MODIFIED="1391021209801">
<node TEXT="Has flower-platform-repo nature" ID="ID_555139699" CREATED="1391021210999" MODIFIED="1391021222821"/>
<node TEXT="Config file: flower-platform-repo.config" ID="ID_513509969" CREATED="1391021223079" MODIFIED="1391021298992">
<node TEXT="XML/EMF" ID="ID_240401741" CREATED="1391021300320" MODIFIED="1391021306372"/>
<node TEXT="Project" ID="ID_1930521111" CREATED="1391021306780" MODIFIED="1391021317132">
<node TEXT="path" ID="ID_685404567" CREATED="1391021321890" MODIFIED="1391021351632"/>
<node TEXT="srcDirs" ID="ID_599830317" CREATED="1391021352960" MODIFIED="1391021358562"/>
</node>
<node TEXT="SrcDir" ID="ID_626906825" CREATED="1391021359990" MODIFIED="1391021365112">
<node TEXT="path" ID="ID_1471178124" CREATED="1391021366490" MODIFIED="1391021370832"/>
<node TEXT="technologies" ID="ID_833947751" CREATED="1391021372230" MODIFIED="1391021375692"/>
</node>
</node>
<node TEXT="When our nature related listener is invoked =&gt; we load the .config" ID="ID_886446067" CREATED="1391021397330" MODIFIED="1391021479452"/>
<node TEXT="All configs are in a map held by proj name (repo name)" ID="ID_1280798852" CREATED="1391021830990" MODIFIED="1391022177657"/>
</node>
</node>
</node>
<node TEXT="selectie la hover; mai degraba butonul de + la mouse over; spatiu pentru expand" ID="ID_1904358924" CREATED="1391791743343" MODIFIED="1391791780745"/>
<node TEXT="undo la operatiuni" ID="ID_1925277911" CREATED="1390239760313" MODIFIED="1390239765045"/>
<node TEXT="Editor de fisiere" ID="ID_442917280" CREATED="1393273920472" MODIFIED="1393273923249"/>
<node TEXT="Mindmap" ID="ID_292123251" CREATED="1393929303206" MODIFIED="1393929304838">
<node TEXT="dialog de editare (cam exista acum, dar de cizelat putin?)" ID="ID_491060427" CREATED="1393929306026" MODIFIED="1393929325278"/>
<node TEXT="editor de stiluri" ID="ID_848991306" CREATED="1393929332486" MODIFIED="1393929340648"/>
<node TEXT="cut/copy/paste" ID="ID_1043266665" CREATED="1393929343656" MODIFIED="1393929347388">
<node TEXT="implica si sistemul de move, care a fost pus in stand by" ID="ID_1314002821" CREATED="1393929348586" MODIFIED="1393929438198"/>
</node>
<node TEXT="links" ID="ID_1590884843" CREATED="1393929440826" MODIFIED="1393929442488"/>
<node TEXT="connectors" ID="ID_635580361" CREATED="1393929445866" MODIFIED="1393929452368"/>
<node TEXT="stiluri" ID="ID_405398089" CREATED="1393929474547" MODIFIED="1393929475739">
<node TEXT="ce a mai ramas de prin editor/meniu" ID="ID_1202783367" CREATED="1393929476537" MODIFIED="1393929483019"/>
<node TEXT="pt edge" ID="ID_310666482" CREATED="1393929483227" MODIFIED="1393929488439"/>
</node>
<node TEXT="zoom" ID="ID_1461646706" CREATED="1393929492787" MODIFIED="1393929494729"/>
<node TEXT="nota/node details" ID="ID_1770182300" CREATED="1393929499897" MODIFIED="1393929514899"/>
<node TEXT="atribute" ID="ID_454331221" CREATED="1393929548717" MODIFIED="1393929550719"/>
</node>
<node TEXT="Customized create actions and dialogs" FOLDED="true" ID="ID_1016473802" CREATED="1396002836216" MODIFIED="1396002847937">
<node TEXT="E.g. File, Directory: don&apos;t show the &quot;isDirectory&quot;" ID="ID_1664647015" CREATED="1396002848946" MODIFIED="1396002861327"/>
<node TEXT="Mindmap: append automatically .mm, so that the addController can do it&apos;s job" ID="ID_322522820" CREATED="1396002861695" MODIFIED="1396002885217"/>
<node TEXT="Idem: for &quot;create java file&quot;" ID="ID_946790536" CREATED="1396002885436" MODIFIED="1396002922668"/>
<node TEXT="Future: do we want in creation wizard? E.g. creation wizard?" ID="ID_751919689" CREATED="1396002923007" MODIFIED="1396002949588"/>
<node TEXT="Idea" ID="ID_1593851672" CREATED="1396002966077" MODIFIED="1396002967568">
<node TEXT="The &quot;create&quot; descriptor, should have a subtype. E.g. &quot;mm&quot;." ID="ID_1897069704" CREATED="1396003049198" MODIFIED="1396003069169"/>
<node TEXT="Get the Flex UI, based on type: &quot;file.mm&quot;" ID="ID_800154485" CREATED="1396002968367" MODIFIED="1396003078189"/>
<node TEXT=".all =&gt; generic add" ID="ID_1092823757" CREATED="1396003078417" MODIFIED="1396003086749"/>
<node TEXT="customize for the examples above" ID="ID_1489685820" CREATED="1396003087017" MODIFIED="1396003100159"/>
</node>
<node TEXT="Remove the hardcoded label from the current version (&quot;please append .mm...&quot;)" ID="ID_1483096619" CREATED="1396003115588" MODIFIED="1396003130269"/>
</node>
<node TEXT="MindMap Styles editor" ID="ID_1945079451" CREATED="1396010539980" MODIFIED="1396010552272">
<node TEXT="What do we do when MM in database? Regarding style system?" ID="ID_1650781518" CREATED="1396010553630" MODIFIED="1396010567472"/>
</node>
</node>
</node>
<node TEXT="Elemente care ne trebuie pe viitor&#xa;(nu jeneaza in versiunile interne)" ID="ID_913616212" CREATED="1392200850907" MODIFIED="1392200868989">
<node TEXT="DB version" ID="ID_770495156" CREATED="1394039713346" MODIFIED="1394039727717">
<node TEXT="SessionUnsubscriber" ID="ID_70594210" CREATED="1394039738026" MODIFIED="1394039808337">
<node TEXT="e costisitor zic eu, daca se uita la intreaga baza" ID="ID_1317436529" CREATED="1394039808886" MODIFIED="1394039827757"/>
<node TEXT="ar trebui ca fiecare server sa cunoasca doar ses lui, si sa se ocupe doar de ele" ID="ID_826187789" CREATED="1394039828026" MODIFIED="1394039844498"/>
<node TEXT="altfel, ar trebui ca doar un server sa faca unsub" ID="ID_878474750" CREATED="1394039845047" MODIFIED="1394039854428"/>
<node TEXT="o alta idee: la ping, sa se scrie intr-o tabela; apoi noi tot facem cerere cu data &lt; data curente - x" ID="ID_1528720863" CREATED="1394643253915" MODIFIED="1394643439340"/>
</node>
</node>
<node TEXT="Port the permissions system" ID="ID_138844554" CREATED="1392200870477" MODIFIED="1392200926189">
<node TEXT="As we will display the contents of the file system, we want to make sure no access is possible outside the workspace." ID="ID_73561095" CREATED="1392200926897" MODIFIED="1392200984989"/>
<node TEXT="Even in .metadata" ID="ID_1061647885" CREATED="1392200985297" MODIFIED="1392200991129"/>
<node TEXT="And we want to have a basic permission model" ID="ID_242982827" CREATED="1392200991617" MODIFIED="1392201243869"/>
<node TEXT="And a customizable one (Enterprise/Cloud version)." ID="ID_739678597" CREATED="1392201243869" MODIFIED="1392201243869"/>
</node>
</node>
<node TEXT="Idei" ID="ID_1113413533" CREATED="1394111024763" MODIFIED="1394111025814">
<node TEXT="Sistem de adaugat badge in readme.md, asa cum fac si cei de la waffle.io" ID="ID_92016152" CREATED="1394111026743" MODIFIED="1394111041464"/>
<node TEXT="Sistem de messaging direct in aplicatie. Tot ca in waffle.io. Mi s-a parut interesant ca la un moment dat au cerut sa vorbim (zicand apoi ca ne baga in lista VIP, free swag, etc)" ID="ID_1955369306" CREATED="1397556492560" MODIFIED="1397556597602"/>
</node>
<node TEXT="Stagiari" ID="ID_521994348" CREATED="1397574051261" MODIFIED="1397574052662">
<node TEXT="Framework workbench JS" ID="ID_937010514" CREATED="1397574053441" MODIFIED="1397574066412"/>
<node TEXT="Librarie de diagrame JS, bazata pe SVG" ID="ID_1772864255" CREATED="1397574066780" MODIFIED="1397574074052"/>
</node>
</node>
<node TEXT="De documentat" ID="ID_1953554231" CREATED="1392303595601" MODIFIED="1395134472262">
<node TEXT="setup mediul de lucru" ID="ID_502561875" CREATED="1393258784674" MODIFIED="1393258787653">
<node TEXT="de folosit platforma noua ca runtime, chiar si pentru cei cu ecl 3.7" ID="ID_1767754715" CREATED="1391442144893" MODIFIED="1391442164795"/>
<node TEXT="de modificat setarea pentru persistenta sesiunilor" ID="ID_45698908" CREATED="1392715932393" MODIFIED="1392715946124"/>
<node TEXT="la instalare flex: run as admin" ID="ID_699218521" CREATED="1393258793749" MODIFIED="1393258807291"/>
<node TEXT="proprietati .ini" ID="ID_1309177137" CREATED="1393325097607" MODIFIED="1393325104077">
<node TEXT="-Djava.util.Arrays.useLegacyMergeSort=true" ID="ID_756935028" CREATED="1393325146005" MODIFIED="1393325148307"/>
<node TEXT="-Duser.name=..." ID="ID_771570103" CREATED="1393325175625" MODIFIED="1393325182557"/>
<node TEXT="In caz de probleme, se poate lansa un eclipse -clean; are legatura cu schimbarea de sdk. Alta simptoma: nu se mai colora sintaxa in mxml" ID="ID_1622162306" CREATED="1393325112505" MODIFIED="1393325276629"/>
</node>
<node TEXT="atentie la cei care au eclipse + FB, se adauga anumite pluginuri in duplicat, pentru ca in config sunt specificate doar dupa nume. El gaseste 2, si le iau pe amandoua." ID="ID_93907515" CREATED="1397642828706" MODIFIED="1397642872405"/>
<node TEXT="de pus pluginul cu culorile de consola" ID="ID_1590199578" CREATED="1397642898559" MODIFIED="1397642907232"/>
</node>
<node TEXT="de recomandat Prune Git din cand in cand" ID="ID_79245536" CREATED="1392717191766" MODIFIED="1392717192658"/>
<node TEXT="NodePropertiesConstants" ID="ID_1170844894" CREATED="1392994960059" MODIFIED="1392994965421">
<node TEXT="Ar trebui poate sa avem o clasa de constante per plugin mai bine?" ID="ID_1127104547" CREATED="1393665298610" MODIFIED="1393665310391"/>
</node>
<node TEXT="readme.md pentru fiecare proiect nou" ID="ID_1053399333" CREATED="1392720450636" MODIFIED="1392720458057"/>
<node TEXT="Structura proj" ID="ID_1872197900" CREATED="1395230057262" MODIFIED="1395230060180">
<node TEXT="constante" ID="ID_462642284" CREATED="1395230061908" MODIFIED="1395230063291"/>
<node TEXT="Utils" ID="ID_1854870355" CREATED="1395230158575" MODIFIED="1395230159888">
<node TEXT="De scanat pentru Utils; static...: si refactor" ID="ID_537762237" CREATED="1395415599376" MODIFIED="1395415608619"/>
</node>
<node TEXT="Plugin" ID="ID_258351087" CREATED="1395230170443" MODIFIED="1395230171723"/>
<node TEXT="Flex: configurare path" ID="ID_398547508" CREATED="1395230173320" MODIFIED="1395230182406"/>
<node TEXT="readme.md" ID="ID_1503649956" CREATED="1395230063529" MODIFIED="1395230071229">
<node TEXT="proj" ID="ID_315318682" CREATED="1395230071896" MODIFIED="1395230073014"/>
<node TEXT="icons" ID="ID_815913218" CREATED="1395230073220" MODIFIED="1395230074155"/>
<node TEXT="lib" ID="ID_1359842895" CREATED="1395230080996" MODIFIED="1395230083004"/>
</node>
</node>
</node>
<node TEXT="De facut Unit test" ID="ID_1501504796" CREATED="1392654059724" MODIFIED="1395134479983">
<node TEXT="Unit test for codeSync markers propagation" ID="ID_103616921" CREATED="1392654062574" MODIFIED="1392654204277">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_1536200420" STARTINCLINATION="4439;0;" ENDINCLINATION="4439;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
<node TEXT="Mai toate functiile din NodeService. Au fost incepute de Sebi" ID="ID_1024523984" CREATED="1392719660361" MODIFIED="1392720099354"/>
<node TEXT="Logica din RootNodeDAO" ID="ID_1361447725" CREATED="1392911009278" MODIFIED="1392911015240"/>
<node TEXT="codesync/propagare markeri" ID="ID_868195565" CREATED="1394441193854" MODIFIED="1394441200645"/>
</node>
<node TEXT="Probleme relativ urgente" ID="ID_1110764825" CREATED="1392394143186" MODIFIED="1395233858944">
<icon BUILTIN="clanbomber"/>
<node TEXT="Node refresh: keep expanded children" ID="ID_994075515" CREATED="1394638526583" MODIFIED="1394638550084">
<node TEXT="se intampla sau nu? mie asa mi s-a parut ca se intampla (i.e. BUG)" ID="ID_924705524" CREATED="1395231439268" MODIFIED="1395231454599"/>
</node>
<node TEXT="robustete la parametrii aiurea" ID="ID_68031472" CREATED="1394640036148" MODIFIED="1394640042050">
<node TEXT="RawNodeDataProvider" ID="ID_367699444" CREATED="1397565222922" MODIFIED="1397565243203">
<node TEXT="apelat sistematic" ID="ID_135126494" CREATED="1397565244451" MODIFIED="1397565259753"/>
<node TEXT="are rolul de a valida ca elementul exista" ID="ID_1344958050" CREATED="1397565259971" MODIFIED="1397565272333"/>
<node TEXT="are rolul de a verifica ca tipul din nod == tipul real, persistat in resursa" ID="ID_597000450" CREATED="1397565273011" MODIFIED="1397565300283"/>
</node>
<node TEXT="AddNodeController" ID="ID_1450549498" CREATED="1397565303071" MODIFIED="1397565317593">
<node TEXT="de verificat ca de fapt creeez doar nod pentru care am descriptor" ID="ID_1259628330" CREATED="1397565318372" MODIFIED="1397565337293"/>
<node TEXT="e.g. nu &quot;file&quot; intr-un mindMap" ID="ID_470654467" CREATED="1397565337671" MODIFIED="1397565344903"/>
</node>
</node>
<node TEXT="modificari de size mobile: in functie de css/dpi" ID="ID_691490068" CREATED="1394724135091" MODIFIED="1394724208061"/>
<node TEXT="drag &amp; drop node" ID="ID_265071183" CREATED="1393273253446" MODIFIED="1393273258243"/>
<node TEXT="Eclipse/jetty: trebuie rulate extension points pentru inregistrat listener" ID="ID_808976886" CREATED="1393949880557" MODIFIED="1393949894639"/>
<node TEXT="Thread safeness" FOLDED="true" ID="ID_1422718171" CREATED="1392394299306" MODIFIED="1393269890273">
<icon BUILTIN="narrative"/>
<node TEXT="la nivel de serviciu? controller? rootnode?" ID="ID_910353587" CREATED="1392395971871" MODIFIED="1392395998463"/>
<node TEXT="Presupunem ca exista niste operatiuni concurente" ID="ID_486039163" CREATED="1392903110359" MODIFIED="1392903141179">
<node TEXT="Operatiuni punctuale din UI" ID="ID_878203498" CREATED="1392903142708" MODIFIED="1392903259162">
<node TEXT="Add, add" ID="ID_877388240" CREATED="1392903260391" MODIFIED="1392903279692">
<node TEXT="In principiu nu pune probleme." ID="ID_909677979" CREATED="1392903280351" MODIFIED="1392903332826"/>
</node>
<node TEXT="Remove parent, add to parent" ID="ID_1257181995" CREATED="1392903926847" MODIFIED="1392903933628">
<node TEXT="Avem o problema, deci ar trebui ca serverul sa se astepte la acest lucru, si sa &quot;fail gracefully&quot;" ID="ID_1099861207" CREATED="1392903934517" MODIFIED="1392903962508"/>
</node>
<node TEXT="Remove, set property" ID="ID_3236797" CREATED="1392903963847" MODIFIED="1392903968918">
<node TEXT="Idem" ID="ID_1788258682" CREATED="1392903970197" MODIFIED="1392903971868"/>
</node>
<node TEXT="Set property, Set property" ID="ID_439841358" CREATED="1392903975807" MODIFIED="1392903982898">
<node TEXT="Proprietati distincte: nici o pb" ID="ID_492460091" CREATED="1392904094066" MODIFIED="1392904120188"/>
<node TEXT="Daca vorbim de ac. proprietate, evident exista un conflict, caci doar unul va castiga (ultimul). Oricum, si cu lock, tot doar unul castiga, insa primul. In 90% din cazuri, userul 2 ar reincerca si oricum va castiga." ID="ID_224641049" CREATED="1392903983757" MODIFIED="1392904092048"/>
</node>
</node>
<node TEXT="Logica cu operatiuni compuse; e.g. read -&gt; logica -&gt; then actiune" ID="ID_310842948" CREATED="1392904135646" MODIFIED="1392904164108">
<node TEXT="Insa in operatiuni de tip citire -&gt; logica then add, acest lucru evident poate esua. E.g. creere de director, sau alte operatiuni de adaugare unica. Poate si creare de element CodeSync. In cazul directorului, ar esua operatiunea de creere. Dar in cazul element CS, nu ar esua, si ne-am regasi cu 2 elemente &quot;unice&quot;." ID="ID_780017982" CREATED="1392903336855" MODIFIED="1392903912298"/>
<node TEXT="Idem si pentru alte cazuri" ID="ID_1060054905" CREATED="1392904179846" MODIFIED="1392904187498"/>
</node>
<node TEXT="In implementarea de FS/in memory, ar putea da eventual o &quot;concurrent access&quot; pt liste; care insa nu e un capat de tara (e.g. se adauga in timp ce cineva cere copiii)." ID="ID_859798537" CREATED="1392905090775" MODIFIED="1392905130128"/>
</node>
<node TEXT="CodeSync: clar, pune lock la nivel de repo" ID="ID_65827406" CREATED="1392904355267" MODIFIED="1392904363549"/>
<node TEXT="Propunere" ID="ID_187069097" CREATED="1392904220016" MODIFIED="1392904222098">
<node TEXT="Operatiunile punctuale (NodeService) nu se bazeaza pe lock. Insa trebuie sa stie ca pot esua, deci fail gracefully. Insa, ele ar trebui sa tina cont de lock. I.e.: daca exista, sa nu faca nimic" ID="ID_1887814746" CREATED="1392904223147" MODIFIED="1392904409840"/>
<node TEXT="Alte coduri, ar avea 2 optiuni" ID="ID_1006253736" CREATED="1392904250396" MODIFIED="1392904260428">
<node TEXT="Sa se astepte ca pot esua, deci fail gracefully" ID="ID_806214808" CREATED="1392904277236" MODIFIED="1392904299208"/>
<node TEXT="Sa puna un lock" ID="ID_541062803" CREATED="1392904299696" MODIFIED="1392904388200">
<font BOLD="true"/>
<node TEXT="Cum?" ID="ID_634517136" CREATED="1392904321387" MODIFIED="1392904324079"/>
<node TEXT="In BD e un pic delicat. Deja, daca cineva incearca si lock-ul e inchis, nu avem ca in java reluarea automata a actiunilor. Ar esua, si ar trebui noi sa reincercam." ID="ID_1157403526" CREATED="1392904324298" MODIFIED="1392904518181"/>
<node TEXT="Daca serverul care a pus lock crapa, atunci lockul va ramane vesnic. Deci trebuie o procesare de background care sa scoata lockul (watch-dog). Iar lockerul sa ping-uiasca periodic locul, ca sa nu vina watch dog-ul sa-l ridice" ID="ID_905129568" CREATED="1392904518679" MODIFIED="1392904616671"/>
<node TEXT="De fapt eu nu cunosc cum functioneaza replicarea BD. Caci noi presupunem aici ca BD e sfanta. Dar poate nu e asa. Adica si ea are o latente. Si poate se intampla o treaba concurenta, pe servere diferite. Si lock-ul va esua, caci ambii au pus lock in acelasi timp, insa nu a ajuns inca informatia dintre data centere." ID="ID_1052273900" CREATED="1392904620829" MODIFIED="1392904727951"/>
</node>
</node>
</node>
<node TEXT="Avem probleme si pe la typeDescriptor" ID="ID_1999448501" CREATED="1396440519228" MODIFIED="1396440525640"/>
</node>
<node TEXT="de regrupat undeva ca doc responsabilitatile unei resurse (controlleri, etc)" ID="ID_1846209472" CREATED="1392906267032" MODIFIED="1392906287584"/>
<node TEXT="Node doesn&apos;t exist: what do we do?" ID="ID_134605191" CREATED="1392653985922" MODIFIED="1392653994204">
<node TEXT="Exception" ID="ID_1608816936" CREATED="1392653995052" MODIFIED="1392653996864"/>
<node TEXT="Silent fail + log?" ID="ID_734493728" CREATED="1392653997372" MODIFIED="1392654003944"/>
<node TEXT="Resolve on translate fullNodeId -&gt; node? how about server?" ID="ID_1056412777" CREATED="1392654004912" MODIFIED="1392654030774"/>
<node TEXT="validare de nod ca exista. Ar merge in rawNodeDataProv? Poate chiar mai devreme?" ID="ID_424936057" CREATED="1394034967299" MODIFIED="1394034998701"/>
</node>
<node TEXT="de verificat .md pentru proiecte?" ID="ID_1471317361" CREATED="1392720441356" MODIFIED="1392720447387"/>
<node TEXT="Audit al modului in care se declara descriptorii" ID="ID_242088482" CREATED="1392794491200" MODIFIED="1392794517331">
<node TEXT="Lizibilitate?" ID="ID_355038261" CREATED="1392794517915" MODIFIED="1392794520537"/>
<node TEXT="Am impresia ca sistemul vechi de CS Descriptor parca era mai citet?" ID="ID_1615908680" CREATED="1392794520794" MODIFIED="1392794546379"/>
<node TEXT="De reintrodus acele init runnable" ID="ID_927113450" CREATED="1392794547930" MODIFIED="1392794555653"/>
<node TEXT="Sa facem o afisare frumoasa a intregului registru la startup?" ID="ID_1095081416" CREATED="1392794615245" MODIFIED="1392794629239"/>
<node TEXT="Sa grupam toti descriptorii la un loc?" ID="ID_1762207939" CREATED="1392794719344" MODIFIED="1392794727551"/>
</node>
<node TEXT="documentare API NodeService, ResourceService" ID="ID_1424480617" CREATED="1395233526323" MODIFIED="1395233533696">
<node TEXT="de regrupat undeva ca doc responsabilitatile unei resurse (controlleri, etc)" ID="ID_1542787273" CREATED="1392906267032" MODIFIED="1392906287584"/>
</node>
<node TEXT="resurse read only" ID="ID_815268101" CREATED="1395233480081" MODIFIED="1395233484038"/>
<node TEXT="Editor de diagrama" ID="ID_417928541" CREATED="1394809077302" MODIFIED="1394809083402">
<node TEXT="Putem optimiza un pic performanta?" ID="ID_809486142" CREATED="1394809084449" MODIFIED="1394809090868"/>
<node TEXT="pozitionare eronata linii, cand se face reciclare" ID="ID_1221969982" CREATED="1394809091661" MODIFIED="1394809138761"/>
</node>
<node TEXT="FlexDiagram: de facut try/catch. Caci exceptii de pe acolo, blocheaza app iremediabil" ID="ID_343649564" CREATED="1395233891559" MODIFIED="1395233935453"/>
<node TEXT="Flexutil: am putea unifica flexutil.popupFactory, progressMonitor, Alert?" ID="ID_8060037" CREATED="1395415203644" MODIFIED="1395415229702"/>
</node>
<node TEXT="Pentru baza de date" FOLDED="true" ID="ID_926675229" CREATED="1395906697335" MODIFIED="1395906700547">
<node TEXT="Stergerea unui nod =&gt; stergerea nodurilor copii.&#xa;Vom emite notificari si pentru ele?" ID="ID_234362136" CREATED="1395906704385" MODIFIED="1395906740757"/>
</node>
<node TEXT="Colector probleme de utilizare" ID="ID_1978184688" CREATED="1395134724944" MODIFIED="1395233865404">
<icon BUILTIN="smily_bad"/>
<node TEXT="Latency on select item" ID="ID_551968356" CREATED="1395134731573" MODIFIED="1395134753738">
<node TEXT="se vede si la click dreapta direct pe un nod neselectat. Meniul se afiseaza mai greu" ID="ID_561192215" CREATED="1395134785864" MODIFIED="1395134834654"/>
<node TEXT="la click dreapta pe diagama: se afiseaza repede meniul" ID="ID_612186520" CREATED="1395134799880" MODIFIED="1395134825773"/>
</node>
<node TEXT="Latency on change selection (properties editor)" ID="ID_546952239" CREATED="1395134754166" MODIFIED="1395134783006"/>
<node TEXT="Rename -&gt; Edit node core in dialog; + icon" ID="ID_1253951797" CREATED="1395138529133" MODIFIED="1395140337512">
<node TEXT="FreePlane is internationalized? So that we can take the messages from their files?" ID="ID_1606207975" CREATED="1395140338676" MODIFIED="1395140370984"/>
</node>
<node TEXT="Cleanup Freeplane" ID="ID_1532739200" CREATED="1396003175749" MODIFIED="1396003180621">
<node TEXT="remove freeplaneService" STYLE_REF="Done (Blue)" ID="ID_72211022" CREATED="1396003181399" MODIFIED="1399462781826"/>
<node TEXT="remove &quot;FreePlaneUtils&quot;" STYLE_REF="Done (Blue)" ID="ID_1733887642" CREATED="1396003191480" MODIFIED="1399462782877"/>
<node TEXT="Problem in FreepResChPv" ID="ID_1198549466" CREATED="1396003692023" MODIFIED="1396003704945">
<node TEXT="If other file resources are subscribable =&gt; issue" ID="ID_752828333" CREATED="1396003706023" MODIFIED="1396003715345"/>
<node TEXT="Solve: look at extension" ID="ID_375632777" CREATED="1396003727743" MODIFIED="1396003734025"/>
</node>
<node TEXT="rename isSubscribable -&gt; isResource" ID="ID_14244009" CREATED="1396003870455" MODIFIED="1396003879267"/>
</node>
<node TEXT="log: .ping pollutes log" ID="ID_774117253" CREATED="1397642771375" MODIFIED="1397642803922"/>
<node TEXT="de discutat" ID="ID_852255382" CREATED="1397643784847" MODIFIED="1397643790260">
<node TEXT="Mariana" ID="ID_141938240" CREATED="1397643792649" MODIFIED="1397643794571">
<node TEXT="cum folosim spinner la reconectare; poate de refolosit cel cu logo?" ID="ID_224387971" CREATED="1397643795429" MODIFIED="1397643805557"/>
<node TEXT="O problema la spinner: vad ca il folosim pe cel MX si pe mobil. Parca era ceva deja facut pt. mobil" ID="ID_1017949763" CREATED="1397643823218" MODIFIED="1397643846443"/>
</node>
</node>
<node TEXT="Spinner, gh163" ID="ID_901781026" CREATED="1397643449730" MODIFIED="1397643490922">
<node TEXT="Avem ModalSpinner, ModalSpinnerSupport. Un mecansim din MX, prin care puneam usor spinner global sau pe ferestre. Ar trebui facut compatibil si cu mobilul. Adica acum, cum view-urile sunt active fie pe mobil fie pe desktop, sa mearga pentru ambele." ID="ID_1844171703" CREATED="1397643492342" MODIFIED="1397643732809"/>
<node TEXT="Am putea folosi un spinner HTML. Astfel, browserul fiind pe alt thread, poate nu s-ar gatui spre sfarsit." ID="ID_1013598124" CREATED="1397643734017" MODIFIED="1397643760380"/>
<node TEXT="Si oricum, cum preconizam sa avem in general view-uri HTML, interesul spinnerului de Flex scade." ID="ID_1682624692" CREATED="1397643760588" MODIFIED="1397643779074"/>
</node>
<node TEXT="Dialogul de new mm" ID="ID_1991845951" CREATED="1397646763480" MODIFIED="1397646772713">
<node TEXT="titlul cu n mic" ID="ID_164237222" CREATED="1397646773632" MODIFIED="1397646780695"/>
<node TEXT="de pus mai proeminent warningul de .mm" ID="ID_1383603780" CREATED="1397646781174" MODIFIED="1397646796029"/>
<node TEXT="nu se creaza decat un fisier gol" ID="ID_1792498201" CREATED="1397646796268" MODIFIED="1397646801520"/>
</node>
<node TEXT="Dialogul Open in new editor" ID="ID_596260355" CREATED="1397646875174" MODIFIED="1397646909251">
<node TEXT="cam uratel" ID="ID_1114720428" CREATED="1397646910070" MODIFIED="1397646911981"/>
<node TEXT="am putea butoane in footer?" ID="ID_978425658" CREATED="1397646912739" MODIFIED="1397646920573"/>
<node TEXT="Cancel ar trebui sa fie &quot;No&quot;" ID="ID_389690482" CREATED="1397646922741" MODIFIED="1397646930756"/>
<node TEXT="Sa nu uitam sa punem remember in optiuni; poate ar fi bine de dezactivat pana atunci" ID="ID_450239290" CREATED="1397646931034" MODIFIED="1397646965485"/>
</node>
<node TEXT="Content types: iconite pentru noduri cunoscute (e.g. mm)" ID="ID_1214601316" CREATED="1397646815732" MODIFIED="1397647001883"/>
<node TEXT="Titlu taburi" ID="ID_1049580290" CREATED="1397647002191" MODIFIED="1397647005163"/>
<node TEXT="pasul de scroll din rotita e prea mic" ID="ID_241929479" CREATED="1397647158283" MODIFIED="1397647168728"/>
<node TEXT="Buton de save" ID="ID_1573693673" CREATED="1397647241093" MODIFIED="1397647246076"/>
<node TEXT="Tableta" ID="ID_915042599" CREATED="1397647573233" MODIFIED="1397647574924">
<node TEXT="Cand pornesc tableta si serverul nu e pornit: primesc niste erori flex (in debugger).&#xa;Aplicatia ar trebui sa detecteze cand nu exista conexiune. E.g. cand schimbam tableta catre cineva care ar e alt URL" ID="ID_1898402134" CREATED="1397647575972" MODIFIED="1397647682284">
<node TEXT="Numele nu e bun &quot;Open Accounts&quot;" ID="ID_1474406668" CREATED="1397647875833" MODIFIED="1397648171836"/>
</node>
<node TEXT="Avem 2 intrari cu ac. nume. Si bifa e la ambele" ID="ID_1632883226" CREATED="1397647830775" MODIFIED="1397647844248"/>
<node TEXT="Sa afisam spinnerul mai mult" ID="ID_389254978" CREATED="1397647741184" MODIFIED="1397647749522"/>
<node TEXT="Ecranul de gestiune de conturi" ID="ID_1745400681" CREATED="1397647868730" MODIFIED="1397647874927">
<node TEXT="Nu ar trebui inaltime un pic mai mare?" ID="ID_1308452884" CREATED="1397647844589" MODIFIED="1397647855908"/>
<node TEXT="Actiuni pe task bar" ID="ID_1692666624" CREATED="1397647887479" MODIFIED="1397647891947"/>
<node TEXT="Editare" ID="ID_444526246" CREATED="1397647970666" MODIFIED="1397647980252">
<node TEXT="ar trebui o actiune" ID="ID_414054972" CREATED="1397647981150" MODIFIED="1397647985664"/>
<node TEXT="si sa mearga la click pe tot rendererul, nu doar pe label" ID="ID_943146565" CREATED="1397647985973" MODIFIED="1397647995922"/>
</node>
<node TEXT="de ce sunt afisate @...?" ID="ID_1845740497" CREATED="1397648174161" MODIFIED="1397648180738"/>
<node TEXT="De pus o iconita" ID="ID_1034551482" CREATED="1397648181071" MODIFIED="1397648183791"/>
<node TEXT="Dupa modificare cont, trebuie sa resetez. O alta problema: nu pot sa ma conectez la un url non-default, fara a selecta default si a reseta" ID="ID_1686309260" CREATED="1397648208051" MODIFIED="1397648246386"/>
<node TEXT="Apendeaza automat un / la ce am introdus eu. De asemenea, daca am //" ID="ID_1015311445" CREATED="1397648412923" MODIFIED="1397648864121"/>
</node>
<node TEXT="Alerte cam uratele, caci ocupa tot ecranul" ID="ID_453073865" CREATED="1397648107957" MODIFIED="1397648115499"/>
<node TEXT="Dezactivare IED inline; de facut sa mearga ca un popup. Parca aveam ceva facut." ID="ID_661104594" CREATED="1397649316957" MODIFIED="1397649335653"/>
</node>
<node TEXT="nodul styles" ID="ID_1058324470" CREATED="1397650119174" MODIFIED="1397650122396">
<node TEXT="de afisat primul" ID="ID_1155945853" CREATED="1397650123325" MODIFIED="1397650126687"/>
<node TEXT="de afisat disctinctiv (culoare fundal?)" ID="ID_1871582919" CREATED="1397650126895" MODIFIED="1397650135759"/>
</node>
<node TEXT="Lipseste &quot;add after&quot;" ID="ID_822377678" CREATED="1397650151160" MODIFIED="1397650156153"/>
<node TEXT="fileSystem" ID="ID_1892027839" CREATED="1397651089059" MODIFIED="1397651090862">
<node TEXT="trebuie iconita, titlu frumos" ID="ID_1455452537" CREATED="1397651091538" MODIFIED="1397651096371"/>
<node TEXT="intr-adevar vad ca nu are in id locul repo; pentru momentul in care vom avea multi repo" ID="ID_160550506" CREATED="1397651096619" MODIFIED="1397651157669"/>
</node>
<node TEXT="De facut script de build" ID="ID_367068052" CREATED="1397654247929" MODIFIED="1397654251443">
<node TEXT="Folosim maven? tycho?" ID="ID_165827729" CREATED="1397654252631" MODIFIED="1397654262339"/>
<node TEXT="De bagat versiunea app + API" ID="ID_258511057" CREATED="1397654262597" MODIFIED="1397654279895"/>
<node TEXT="si scripturi de rulat teste" ID="ID_1909281685" CREATED="1397654357277" MODIFIED="1397654384865"/>
</node>
<node TEXT="BUG: After new node created, it seems to have null props" STYLE_REF="Done (Blue)" ID="ID_677258935" CREATED="1397649122171" MODIFIED="1399462787960">
<node TEXT="probleme la ied: pare sa dea null" ID="ID_659703295" CREATED="1397649251427" MODIFIED="1397649259815"/>
</node>
</node>
</node>
<node TEXT="mindmap" STYLE_REF="In progress (Yellow)" POSITION="right" ID="ID_240272065" CREATED="1392370200375" MODIFIED="1393928645546"/>
<node TEXT="users" STYLE_REF="Planned (Orange)" FOLDED="true" POSITION="right" ID="ID_1386339458" CREATED="1396437660994" MODIFIED="1396438569344">
<node TEXT="GH191 - Hello World with JS and JSON API" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_668771924" CREATED="1396436254611" MODIFIED="1396437809821" LINK="https://github.com/flower-platform/flower-platform-4/issues/191">
<node TEXT="Use Jersey to expose remote services as JSON" ID="ID_469862114" CREATED="1395328211552" MODIFIED="1395328436106"/>
<node TEXT="Use RemoteMethodInvocationListener" ID="ID_1202889430" CREATED="1395328436574" MODIFIED="1395328447336"/>
<node TEXT="stand-alone" FOLDED="true" ID="ID_1410744240" CREATED="1396436295475" MODIFIED="1396436298629"/>
<node TEXT="embed/web" ID="ID_931589651" CREATED="1396436298930" MODIFIED="1396436303671"/>
<node TEXT="embed/mobile" ID="ID_277174873" CREATED="1396436303886" MODIFIED="1396436306570"/>
<node TEXT="Redmine" ID="ID_1911518276" CREATED="1396531577751" MODIFIED="1396531577761">
<node TEXT="RM9261 - GH191 - Hello World with JS and JSON API" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_506928080" CREATED="1396531577761" MODIFIED="1396531577781" LINK="http://csp1/redmine/issues/9261"/>
<node TEXT="RM9262 - [Review] GH191 - Hello World with JS and JSON API" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_60717948" CREATED="1396531578111" MODIFIED="1396531578111" LINK="http://csp1/redmine/issues/9262"/>
</node>
<node TEXT="Notes" ID="ID_717349015" CREATED="1397139738287" MODIFIED="1397139743405">
<node TEXT="REST" FOLDED="true" ID="ID_737536096" CREATED="1397139267991" MODIFIED="1397139364360">
<node TEXT="&gt; Resource PATH should be the name of the service as it was registered (e.g. nodeService, downloadService)" ID="ID_1209137048" CREATED="1397139364421" MODIFIED="1397139364425"/>
<node TEXT="&gt; all service methods should have the PATH = name of the method (e.g. setProperty, getChildren)" ID="ID_223019377" CREATED="1397139364415" MODIFIED="1397139364418"/>
<node TEXT="&gt; using Jackson, subclasses are serialized correctly without any extra work; deserialization might be tricky, if we need to send a subclass from client" ID="ID_565710614" CREATED="1397139364410" MODIFIED="1397139364412"/>
</node>
<node TEXT="JS standalone" ID="ID_1914427389" CREATED="1397139364403" MODIFIED="1397139386714">
<node TEXT="&gt; http://localhost:8080/org.flowerplatform.host.web_app/test.html" ID="ID_1294003226" CREATED="1397139364399" MODIFIED="1397139364401"/>
<node TEXT="&gt; cross-domain POST is seen as OPTIONS?" ID="ID_1403919841" CREATED="1397139364395" MODIFIED="1397139364397"/>
</node>
<node TEXT="JS embed into Flex" ID="ID_158615563" CREATED="1397139364389" MODIFIED="1397139364390">
<node TEXT="&gt; JS app - test.html + test.js + jquery" ID="ID_41173590" CREATED="1397139364385" MODIFIED="1397139364387"/>
<node TEXT="&gt; Flex app - embed.js" ID="ID_1103361005" CREATED="1397139364381" MODIFIED="1397139364383"/>
<node TEXT="&gt; added EmbedViewProvider + EmbedView" ID="ID_924325331" CREATED="1397139364377" MODIFIED="1397139364379"/>
<node TEXT="&gt; call JS function directly from Flex - ok" ID="ID_994581803" CREATED="1397139364373" MODIFIED="1397139364375"/>
<node TEXT="&gt; call JS function with a Flex callback (async) - ok, via callFlexCallback" ID="ID_143736139" CREATED="1397139364370" MODIFIED="1397139364372"/>
<node TEXT="&gt; override JS function - ok, if you need &quot;super&quot;, then the JS function should have a do* function. Also, this means that the original function can&apos;t be called anymore" ID="ID_1308309696" CREATED="1397139364367" MODIFIED="1397139364368"/>
</node>
<node TEXT="JS embed into mobile" ID="ID_1138771204" CREATED="1397139364365" MODIFIED="1397139364366">
<node TEXT="&gt; + embed-mobile.js + StageWebViewBridge.js" ID="ID_1197944" CREATED="1397139364364" MODIFIED="1397139364364"/>
<node TEXT="&gt; StageWebView idem as above" ID="ID_490004090" CREATED="1397139364363" MODIFIED="1397139364363"/>
</node>
</node>
<node TEXT="Sincronizare&#xa;15/04/2014" ID="ID_359343709" CREATED="1397567547190" MODIFIED="1397567549241">
<icon BUILTIN="group"/>
</node>
<node TEXT="ecran de users" ID="ID_1800854079" CREATED="1397567553039" MODIFIED="1397567564411">
<node TEXT="http://localhost:8080/org.flowerplatform.host.web_app/js-app/users.html" ID="ID_169883352" CREATED="1397833038607" MODIFIED="1397833039142"/>
</node>
<node TEXT="ecran de editUser" ID="ID_995938786" CREATED="1397567564699" MODIFIED="1397567574631"/>
<node TEXT="Sincronizare" ID="ID_1008403402" CREATED="1397567627321" MODIFIED="1397567627321">
<icon BUILTIN="group"/>
</node>
<node TEXT="navigare smechera?" ID="ID_1714758296" CREATED="1397567578929" MODIFIED="1397567639891"/>
</node>
<node TEXT="GH193 - Analysis: user management" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_21988593" CREATED="1396438090612" MODIFIED="1396438586184" LINK="https://github.com/flower-platform/flower-platform-4/issues/193">
<node TEXT="Community version: file storage" ID="ID_754994422" CREATED="1395330350786" MODIFIED="1395330361168"/>
<node TEXT="Pro version: database" ID="ID_453013393" CREATED="1395330365446" MODIFIED="1395330372698"/>
<node TEXT="HTML &amp; Flex: how does it works in HTML only?&#xa;Adica daca intr-un viitor, vom avea aplicatia full HTML, cum se afiseaza paginile? Cum merge navigarea? La momentul de fata exista acel workbench, si flow bazat pe actiuni, meniu, deschidere de taburi noi" ID="ID_1933534997" CREATED="1396434563944" MODIFIED="1396438209274"/>
<node TEXT="Redmine" FOLDED="true" ID="ID_1158794847" CREATED="1397139791567" MODIFIED="1397139791569">
<node TEXT="RM9305 - GH193 - Analysis: user management" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_643406872" CREATED="1397139791570" MODIFIED="1397139791592" LINK="http://csp1/redmine/issues/9305"/>
<node TEXT="RM9306 - [Review] GH193 - Analysis: user management" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_698652588" CREATED="1397139792196" MODIFIED="1397139792200" LINK="http://csp1/redmine/issues/9306"/>
</node>
<node TEXT="Construction site" ID="ID_1880285233" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="Node" FOLDED="true" ID="ID_591281213" CREATED="1397203348285" MODIFIED="1397203367608">
<node TEXT="User" ID="ID_1681029942" CREATED="1397141618119" MODIFIED="1397141633684">
<node TEXT="type: user" ID="ID_1204572953" CREATED="1397141634122" MODIFIED="1397141636905"/>
<node TEXT="resource: null" ID="ID_118442111" CREATED="1397141639863" MODIFIED="1397142324551"/>
<node TEXT="id: login" ID="ID_1371269624" CREATED="1397141646595" MODIFIED="1397141650027"/>
<node TEXT="properties" ID="ID_1619352128" CREATED="1397141651375" MODIFIED="1397141656967">
<node TEXT="login" ID="ID_1788859051" CREATED="1397141662975" MODIFIED="1397141666027">
<node TEXT="ID - cannot be changed" ID="ID_198956306" CREATED="1397141698399" MODIFIED="1397142863089"/>
</node>
<node TEXT="name" ID="ID_498202761" CREATED="1397141657815" MODIFIED="1397141660747"/>
<node TEXT="email" ID="ID_71968736" CREATED="1397203439064" MODIFIED="1397203440666"/>
<node TEXT="hashedPassword" ID="ID_853879833" CREATED="1397141670305" MODIFIED="1397141676339"/>
<node TEXT="isActivated" ID="ID_350234193" CREATED="1397214627898" MODIFIED="1397214630705"/>
<node TEXT="activationCode" ID="ID_1703050472" CREATED="1397214631062" MODIFIED="1397214634536"/>
<node TEXT="isOrganization : boolean" ID="ID_1334638090" CREATED="1397141677387" MODIFIED="1397141688171"/>
<node TEXT="organizations : List&lt;String&gt;" ID="ID_1645433312" CREATED="1397144408219" MODIFIED="1397144418803">
<node TEXT="organization names; should be enough to just print the list of orgs in the User Form" ID="ID_872852416" CREATED="1397144420241" MODIFIED="1397144482487"/>
</node>
</node>
<node TEXT="children" ID="ID_1925829842" CREATED="1397203976946" MODIFIED="1397203979788">
<node TEXT="repositories" ID="ID_1061808140" CREATED="1397203980966" MODIFIED="1397203995011"/>
<node TEXT="teams" ID="ID_1474730170" CREATED="1397203995589" MODIFIED="1397203998601"/>
<node TEXT="members" ID="ID_1411752230" CREATED="1397217998704" MODIFIED="1397218005786">
<node TEXT="of organization" ID="ID_1616568186" CREATED="1397218007794" MODIFIED="1397218011426"/>
<node TEXT="and of teams of organization" ID="ID_1620118571" CREATED="1397218013744" MODIFIED="1397218020546"/>
</node>
</node>
</node>
<node TEXT="Repository" FOLDED="true" ID="ID_417019706" CREATED="1397203647385" MODIFIED="1397203654637">
<node TEXT="type: repository" ID="ID_856824808" CREATED="1397203691399" MODIFIED="1397203700741"/>
<node TEXT="resource:(user||login)" ID="ID_31989496" CREATED="1397203701919" MODIFIED="1397203711657"/>
<node TEXT="id: name" ID="ID_369074037" CREATED="1397203712155" MODIFIED="1397203781727"/>
</node>
<node TEXT="Team" ID="ID_257621248" CREATED="1397142798031" MODIFIED="1397142803583">
<node TEXT="type: team" ID="ID_1090526700" CREATED="1397142804951" MODIFIED="1397142807463"/>
<node TEXT="resource: (user||login)" ID="ID_514779542" CREATED="1397142808111" MODIFIED="1397142825313">
<node TEXT="the organization&apos;s full id" ID="ID_872427184" CREATED="1397142962538" MODIFIED="1397142971169"/>
</node>
<node TEXT="id: name" ID="ID_1997743511" CREATED="1397142826271" MODIFIED="1397142831769"/>
<node TEXT="properties" ID="ID_987644662" CREATED="1397142832709" MODIFIED="1397142835311">
<node TEXT="name" ID="ID_1359092169" CREATED="1397142836829" MODIFIED="1397142838491">
<node TEXT="ID - cannot be changed" ID="ID_1212770888" CREATED="1397142839459" MODIFIED="1397142856495"/>
</node>
<node TEXT="description" ID="ID_362452366" CREATED="1397205792933" MODIFIED="1397205796067"/>
</node>
<node TEXT="children" ID="ID_61412583" CREATED="1397218036194" MODIFIED="1397218039296">
<node TEXT="members" ID="ID_230859169" CREATED="1397218040384" MODIFIED="1397218042176"/>
</node>
</node>
<node TEXT="Membership" ID="ID_527823224" CREATED="1397216841727" MODIFIED="1397216846028">
<node TEXT="type: membership" ID="ID_1713078157" CREATED="1397216846706" MODIFIED="1397216850578"/>
<node TEXT="resource: (user||login) or (team|(user||login)|name)" ID="ID_435966362" CREATED="1397216851158" MODIFIED="1397221545538">
<node TEXT="the organization or team full id" ID="ID_290373533" CREATED="1397216860860" MODIFIED="1397219189360"/>
</node>
<node TEXT="id: login" ID="ID_730859698" CREATED="1397216870480" MODIFIED="1397219198490"/>
<node TEXT="properties" ID="ID_1164027957" CREATED="1397217236028" MODIFIED="1397217239824">
<node TEXT="status" ID="ID_1748771282" CREATED="1397217240662" MODIFIED="1397217696579">
<node TEXT="ADMIN, MEMBER, PENDING_APPROVAL" ID="ID_89641885" CREATED="1397217697197" MODIFIED="1397217737449"/>
</node>
</node>
</node>
<node TEXT="SAMPLE" FOLDED="true" ID="ID_267832040" CREATED="1397142484171" MODIFIED="1397142490813">
<node TEXT="user||john" ID="ID_1539440848" CREATED="1397142497201" MODIFIED="1397142661494">
<attribute_layout NAME_WIDTH="77" VALUE_WIDTH="77"/>
<attribute NAME="login" VALUE="john"/>
<attribute NAME="name" VALUE="John Doe"/>
<attribute NAME="isOrganization" VALUE="false"/>
<node TEXT="repository|(user||john)|myRepo" ID="ID_1087474882" CREATED="1397204093184" MODIFIED="1397204139378"/>
</node>
<node TEXT="user||jane" ID="ID_1800903479" CREATED="1397142508077" MODIFIED="1397142685118">
<attribute_layout NAME_WIDTH="77" VALUE_WIDTH="77"/>
<attribute NAME="login" VALUE="jane"/>
<attribute NAME="name" VALUE="Jane Doe"/>
<attribute NAME="isOrganization" VALUE="false"/>
</node>
<node TEXT="user||fp" ID="ID_1811088550" CREATED="1397142522669" MODIFIED="1397142714408">
<attribute_layout NAME_WIDTH="77" VALUE_WIDTH="88"/>
<attribute NAME="login" VALUE="fp"/>
<attribute NAME="name" VALUE="Flower Platform"/>
<attribute NAME="isOrganization" VALUE="true"/>
<node TEXT="repository|(user||fp)|main" ID="ID_1484862363" CREATED="1397203793115" MODIFIED="1397203806931"/>
<node TEXT="repository|(user||fp)|other" ID="ID_1132859473" CREATED="1397203868612" MODIFIED="1397217004586"/>
<node TEXT="team|(user||fp)|Owners" ID="ID_1129340428" CREATED="1397143768165" MODIFIED="1397216976476">
<attribute NAME="name" VALUE="Owners"/>
<attribute NAME="description" VALUE="Admins team with absolute permissions"/>
<node TEXT="membership|(team|(user||fp)|Owners)|john" ID="ID_610595098" CREATED="1397217152486" MODIFIED="1397219108920">
<attribute NAME="status" VALUE="ADMIN"/>
</node>
</node>
<node TEXT="team|(user||fp)|RW 1" ID="ID_1982996480" CREATED="1397143768165" MODIFIED="1397217038746">
<attribute NAME="name" VALUE="RW 1"/>
<attribute NAME="description" VALUE="RW permissions over all repos"/>
<node TEXT="membership|(team|(user||fp)|Owners)|john" ID="ID_1289968660" CREATED="1397217152486" MODIFIED="1397219124920">
<attribute NAME="status" VALUE="MEMBER"/>
</node>
<node TEXT="membership|(team|(user||fp)|Owners)|jane" ID="ID_577286770" CREATED="1397217152486" MODIFIED="1397219133620">
<attribute NAME="status" VALUE="MEMBER"/>
</node>
</node>
<node TEXT="team|(user||fp)|RW 2" ID="ID_1094251958" CREATED="1397143768165" MODIFIED="1397217058786">
<attribute NAME="name" VALUE="RW 2"/>
<attribute NAME="description" VALUE="RW permissions over other repo"/>
<node TEXT="membership|(team|(user||fp)|Owners)|jane" ID="ID_1858814457" CREATED="1397217152486" MODIFIED="1397219139870">
<attribute NAME="status" VALUE="MEMBER"/>
</node>
<node TEXT="membership|(team|(user||fp)|Owners)|joe" ID="ID_115285512" CREATED="1397217152486" MODIFIED="1397219145970">
<attribute_layout NAME_WIDTH="76" VALUE_WIDTH="120"/>
<attribute NAME="status" VALUE="MEMBER"/>
</node>
</node>
<node TEXT="membership|(user||fp)|bob" ID="ID_1707275835" CREATED="1397217106806" MODIFIED="1397217888756">
<attribute_layout NAME_WIDTH="49" VALUE_WIDTH="120"/>
<attribute NAME="status" VALUE="PENDING_APPROVAL"/>
</node>
<node TEXT="membership|(user||fp)|mike" ID="ID_1803480731" CREATED="1397217106806" MODIFIED="1397217927726">
<attribute_layout NAME_WIDTH="49" VALUE_WIDTH="120"/>
<attribute NAME="status" VALUE="MEMBER"/>
</node>
</node>
</node>
</node>
<node TEXT="An Organization is a special User, with members and teams. Default team per organization: Owners, with special admin permissions. Members in Owners have the ADMIN status" ID="ID_1329537513" CREATED="1397218257672" MODIFIED="1397218390499"/>
<node TEXT="A user can join an organization from the UI. He is added as a member with PENDING_APPROVAL status. An ADMIN is able to: 1) deny the request =&gt; membership is removed; or 2) approve the request =&gt; user has now MEMBER status and can be added to a team." ID="ID_820926722" CREATED="1397218392177" MODIFIED="1397218500474"/>
<node TEXT="Joining/leaving a team" ID="ID_827383402" CREATED="1397218501642" MODIFIED="1397218511104">
<node TEXT="joining a team" ID="ID_615436596" CREATED="1397218588514" MODIFIED="1397218591988">
<node TEXT="from UI only if the user is a member of the organization. A new membership with PENDING_APPROVAL status is created for the team+user. When it is approved by an admin, the status becomes MEMBER for normal teams, or ADMIN for Owners" ID="ID_1138373755" CREATED="1397218596176" MODIFIED="1397218712662"/>
<node TEXT="if a user joins a team as ADMIN/MEMBER, the membership in the organization is deleted" ID="ID_945153805" CREATED="1397218820897" MODIFIED="1397218846843"/>
<node TEXT="if the action is done by an admin, i.e. add user to organization, the the membership is automatically approved on create" ID="ID_284130389" CREATED="1397218859581" MODIFIED="1397218893107"/>
</node>
<node TEXT="leaving a team" ID="ID_505105689" CREATED="1397218716710" MODIFIED="1397218721880">
<node TEXT="from UI only if the user is a member of the team. Membership team+user is removed" ID="ID_1035483279" CREATED="1397218721882" MODIFIED="1397218742622"/>
</node>
</node>
<node TEXT="Leaving an organization" ID="ID_1239419652" CREATED="1397218750480" MODIFIED="1397218753972">
<node TEXT="all the user&apos;s memberships are automatically deleted, from the org and from teams" ID="ID_53563233" CREATED="1397218756070" MODIFIED="1397218779408"/>
</node>
<node TEXT="Service" FOLDED="true" ID="ID_1472068875" CREATED="1397203384696" MODIFIED="1397203386630">
<node TEXT="UserService" ID="ID_596681487" CREATED="1397205046759" MODIFIED="1397205098874">
<node TEXT="users" ID="ID_996678989" CREATED="1397219449938" MODIFIED="1397219466370">
<node TEXT="newUser()" ID="ID_1804606164" CREATED="1397205100622" MODIFIED="1397225373679"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      POST /users
    </p>
  </body>
</html>
</richcontent>
<hook NAME="FirstGroupNode"/>
<node TEXT="called from UI (register, create new org)" ID="ID_690127330" CREATED="1397205136704" MODIFIED="1397205148298"/>
<node TEXT="delegates to addChild()" ID="ID_874749372" CREATED="1397205149086" MODIFIED="1397205155390"/>
</node>
<node TEXT="getUser()" ID="ID_344499117" CREATED="1397211642756" MODIFIED="1397219835755"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      GET /users/{login}
    </p>
  </body>
</html>
</richcontent>
</node>
<node TEXT="updateUser()" ID="ID_530160488" CREATED="1397211715340" MODIFIED="1397219840155"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      PUT /users/{login}
    </p>
  </body>
</html>
</richcontent>
<node TEXT="called from UI (Update Profile)" ID="ID_1500699957" CREATED="1397211723940" MODIFIED="1397211745932"/>
<node TEXT="delegate to setProperty()" ID="ID_1072253135" CREATED="1397211763850" MODIFIED="1397211769782"/>
<node TEXT="update properties like name, email, avatar, not sensible info like password" ID="ID_1802342725" CREATED="1397224990279" MODIFIED="1397225006583"/>
</node>
<node TEXT="deleteUser()" ID="ID_564927736" CREATED="1397205373640" MODIFIED="1397219844425"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      DELETE /users/{login}
    </p>
  </body>
</html>
</richcontent>
<node TEXT="called from UI (delete account)" ID="ID_877047654" CREATED="1397205392808" MODIFIED="1397205414430"/>
<node TEXT="delegates to removeChild()" ID="ID_1070042689" CREATED="1397205431598" MODIFIED="1397227330121">
<hook NAME="FirstGroupNode"/>
</node>
<node TEXT="delete also all repositories" ID="ID_407624182" CREATED="1397225010931" MODIFIED="1397225031187"/>
<node TEXT="delete all teams, if this was an organization" ID="ID_94983922" CREATED="1397225032723" MODIFIED="1397225046360"/>
<node TEXT="delete all memberships" ID="ID_145482043" CREATED="1397225060460" MODIFIED="1397225066977">
<node TEXT="for each membership, delete the organization from the organizations list of the user that was removed" ID="ID_1156522538" CREATED="1397225066979" MODIFIED="1397225084123"/>
</node>
<node TEXT="RemoveNodeControllers" ID="ID_687306694" CREATED="1397227330120" MODIFIED="1397227357998">
<hook NAME="SummaryNode"/>
</node>
</node>
<node TEXT="CRUD operations for user" ID="ID_415489224" CREATED="1397225373674" MODIFIED="1397225380608">
<hook NAME="SummaryNode"/>
</node>
<node TEXT="getRepositories()" ID="ID_1969409746" CREATED="1397225215244" MODIFIED="1397225506909"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      GET /users/{login}/repositories
    </p>
  </body>
</html>
</richcontent>
<hook NAME="FirstGroupNode"/>
</node>
<node TEXT="getOrganizations()" ID="ID_204004173" CREATED="1397225275128" MODIFIED="1397225320264"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      GET /users/{login}/organizations
    </p>
  </body>
</html>
</richcontent>
<node TEXT="called from UI (user organizations)" ID="ID_579807501" CREATED="1397225322404" MODIFIED="1397225340007"/>
</node>
<node TEXT="getTeams()" ID="ID_1405145957" CREATED="1397225358972" MODIFIED="1397225394736"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      GET /users/{login}/teams
    </p>
  </body>
</html>
</richcontent>
<node TEXT="called from UI (organization teams)" ID="ID_1158512156" CREATED="1397225396140" MODIFIED="1397225404592"/>
</node>
<node TEXT="getMembers()" ID="ID_1694254329" CREATED="1397225439175" MODIFIED="1397225946773"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      GET /users/{login}/memberships
    </p>
  </body>
</html>
</richcontent>
<node TEXT="called from UI (organization members)" ID="ID_776834089" CREATED="1397225472785" MODIFIED="1397225487500"/>
</node>
<node TEXT="Get children for user" ID="ID_1815666711" CREATED="1397225506907" MODIFIED="1397225542527">
<hook NAME="SummaryNode"/>
<node TEXT="these should be added as children providers, and getChildren() should be called with a flag that says which type of child do we want" ID="ID_895353120" CREATED="1397225546532" MODIFIED="1397225591265"/>
</node>
<node TEXT="getUsers()" ID="ID_1482950073" CREATED="1397227097879" MODIFIED="1397227225291"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      GET /users
    </p>
  </body>
</html>
</richcontent>
<hook NAME="FirstGroupNode"/>
<node TEXT="for global admin" ID="ID_21809110" CREATED="1397227123186" MODIFIED="1397227127078"/>
<node TEXT="returns all the users" ID="ID_214292222" CREATED="1397227110854" MODIFIED="1397227122465"/>
</node>
<node TEXT="getOrganizations()" ID="ID_53956573" CREATED="1397227162213" MODIFIED="1397227185995"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      GET /users?organizations
    </p>
  </body>
</html>
</richcontent>
<node TEXT="for global admin" ID="ID_1840836025" CREATED="1397227192648" MODIFIED="1397227203124"/>
<node TEXT="returns all the organizations" ID="ID_1615150275" CREATED="1397227203393" MODIFIED="1397227211395"/>
</node>
<node TEXT="Global admin only. Server-side permissions check" ID="ID_699530409" CREATED="1397227225290" MODIFIED="1397227248492">
<hook NAME="SummaryNode"/>
</node>
</node>
<node TEXT="passwords" FOLDED="true" ID="ID_400177586" CREATED="1397223016777" MODIFIED="1397223271994">
<node TEXT="changePassword()" ID="ID_1409098229" CREATED="1397211892456" MODIFIED="1397223209676"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      PUT /passwords/{login}
    </p>
    <p>
      { oldPassword, newPassword, confirmNewPassword }
    </p>
  </body>
</html>
</richcontent>
<node TEXT="changes the password" ID="ID_990894703" CREATED="1397223059154" MODIFIED="1397223096967"/>
</node>
<node TEXT="resetPassword()" FOLDED="true" ID="ID_1665260983" CREATED="1397223390883" MODIFIED="1397223485069"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      PUT /passwords/{login}
    </p>
    <p>
      null
    </p>
  </body>
</html>
</richcontent>
<node TEXT="remarca FP1: Imbunatatire mecanica de I forgot; cea curenta poate duce catre abuzuri" ID="ID_576684632" CREATED="1359142636480" MODIFIED="1397225137224">
<font BOLD="false"/>
<node TEXT="Problema este ca acum cineva poate reseta altcuiva parola." ID="ID_65593601" CREATED="1359142735677" MODIFIED="1359142754512"/>
<node TEXT="Mai precis resetarea sa se faca dupa ce usreul primeste un email. Noi ii generam un id." ID="ID_748220670" CREATED="1359142663163" MODIFIED="1397225140360"/>
<node TEXT="Cand da click din mail, cu acel id (deci un raufacator nu are de unde sa il stie), abia atunci facem regenerarea." ID="ID_1687783119" CREATED="1359142694153" MODIFIED="1359142735265"/>
</node>
</node>
</node>
<node TEXT="activation" FOLDED="true" ID="ID_155301980" CREATED="1397223227779" MODIFIED="1397223236695">
<node TEXT="activateUser()" ID="ID_1524575322" CREATED="1397222963467" MODIFIED="1397223294298"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      PUT /activation/{login}
    </p>
  </body>
</html>
</richcontent>
<node TEXT="activates the user" ID="ID_1096420396" CREATED="1397223295510" MODIFIED="1397223299719"/>
</node>
<node TEXT="sendActivationCode()" ID="ID_774086234" CREATED="1397223321160" MODIFIED="1397223405161"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      GET /activation/{login}
    </p>
  </body>
</html>
</richcontent>
<node TEXT="sends a mail with the activation code if the user is not yet activated" ID="ID_120072715" CREATED="1397223358855" MODIFIED="1397223377780"/>
</node>
</node>
<node TEXT="repositories" ID="ID_617983380" CREATED="1397219544408" MODIFIED="1397219549910"/>
<node TEXT="teams" FOLDED="true" ID="ID_862968167" CREATED="1397219550678" MODIFIED="1397219552602">
<node TEXT="newTeam()" ID="ID_1098161200" CREATED="1397214744196" MODIFIED="1397225906372"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      POST /users/{login}/teams
    </p>
  </body>
</html>
</richcontent>
<node TEXT="delegates to addChild()" ID="ID_1529134707" CREATED="1397214755500" MODIFIED="1397214762002"/>
</node>
<node TEXT="getTeam()" ID="ID_590778932" CREATED="1397214764322" MODIFIED="1397219863835"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      GET /users/{login}/teams/{name}
    </p>
  </body>
</html>
</richcontent>
</node>
<node TEXT="updateTeam()" ID="ID_1605698103" CREATED="1397214778632" MODIFIED="1397219878085"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      PUT /users/{login}/teams/{name}
    </p>
  </body>
</html>
</richcontent>
</node>
<node TEXT="deleteTeam()" ID="ID_176089485" CREATED="1397214786832" MODIFIED="1397219895737"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      DELETE /users/{login}/teams/{name}
    </p>
  </body>
</html>
</richcontent>
<node TEXT="deletes memberships" ID="ID_1680110928" CREATED="1397227438820" MODIFIED="1397227447772"/>
</node>
<node TEXT="CRUD operations for team" ID="ID_1766538156" CREATED="1397225906370" MODIFIED="1397225914853">
<hook NAME="SummaryNode"/>
</node>
<node TEXT="getMembers()" ID="ID_766661107" CREATED="1397225844776" MODIFIED="1397225953048"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      GET /users/{login}/teams/{name}/memberships
    </p>
  </body>
</html>
</richcontent>
<hook NAME="FirstGroupNode"/>
</node>
<node TEXT="Get children for team" ID="ID_1098738028" CREATED="1397225921114" MODIFIED="1397225933821">
<hook NAME="SummaryNode"/>
</node>
</node>
<node TEXT="memberships" FOLDED="true" ID="ID_896482207" CREATED="1397219519470" MODIFIED="1397219528710">
<node TEXT="addToOrganization()" ID="ID_1973854970" CREATED="1397218096824" MODIFIED="1397226085235"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      POST /users/{login}/memberships
    </p>
  </body>
</html>
</richcontent>
<hook NAME="FirstGroupNode"/>
<node TEXT="creates a new membership in the organization" ID="ID_1796410949" CREATED="1397218103194" MODIFIED="1397220582629"/>
<node TEXT="if join by a user =&gt; status is PENDING" ID="ID_896475870" CREATED="1397225972400" MODIFIED="1397225982680"/>
<node TEXT="if add by an admin =&gt; status is MEMBER" ID="ID_1332000105" CREATED="1397225984021" MODIFIED="1397225991998"/>
<node TEXT="add the organization to the user&apos;s list of organizations" ID="ID_319244723" CREATED="1397226107494" MODIFIED="1397226117837"/>
</node>
<node TEXT="addToTeam()" ID="ID_477619317" CREATED="1397219026614" MODIFIED="1397224570294"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      POST /users/{login}/teams/{name}/memberships
    </p>
  </body>
</html>
</richcontent>
<node TEXT="creates a new membership in the team" ID="ID_992065541" CREATED="1397219035179" MODIFIED="1397220590479"/>
<node TEXT="if join by a user =&gt; status is PENDING" ID="ID_667082776" CREATED="1397225972400" MODIFIED="1397225982680"/>
<node TEXT="if add by an admin =&gt; status is MEMBER" ID="ID_1145062417" CREATED="1397225984021" MODIFIED="1397225991998"/>
<node TEXT="add the organization to the user&apos;s list of organizations" ID="ID_1751172501" CREATED="1397226126333" MODIFIED="1397226134873"/>
</node>
<node TEXT="removeFromOrganization()" ID="ID_199741052" CREATED="1397221435256" MODIFIED="1397224575523"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      DELETE /users/{login}/memberships/{id}
    </p>
  </body>
</html>
</richcontent>
<node TEXT="deletes (membership|(user||login)|id)" ID="ID_1706729179" CREATED="1397221502152" MODIFIED="1397221529454"/>
<node TEXT="only for admin" ID="ID_1970420930" CREATED="1397226016938" MODIFIED="1397226019642"/>
<node TEXT="remove the organization from the user&apos;s list of organizations" ID="ID_826407753" CREATED="1397226143895" MODIFIED="1397226152775"/>
</node>
<node TEXT="removeFromTeam()" ID="ID_1070216702" CREATED="1397221551656" MODIFIED="1397224582105"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      DELETE /users/{login}/teams/{name}/memberships/{id}
    </p>
  </body>
</html>
</richcontent>
<node TEXT="deletes (membership|(team|(user||login)|name)|id)" ID="ID_1030427833" CREATED="1397221590542" MODIFIED="1397221624960"/>
<node TEXT="only for admin" ID="ID_1360099389" CREATED="1397226016938" MODIFIED="1397226019642"/>
<node TEXT="remove the organization from the user&apos;s list of organizations, if this was the only team of the organization where this user belongs" ID="ID_572039267" CREATED="1397226143895" MODIFIED="1397226175235"/>
</node>
<node TEXT="approveMembership()" ID="ID_1066217075" CREATED="1397224084931" MODIFIED="1397224185419"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      PUT /users/{login}/memberships/{id}
    </p>
    <p>
      PUT /users/{login}/teams/{name}/memberships/{id}
    </p>
  </body>
</html>
</richcontent>
<node TEXT="changes the status to MEMBER or ADMIN, depending on team" ID="ID_1767791387" CREATED="1397226033429" MODIFIED="1397226049280"/>
<node TEXT="only if the current status is PENDING" ID="ID_1727241107" CREATED="1397226049664" MODIFIED="1397226056613"/>
</node>
<node TEXT="denyMembership()" ID="ID_1741233587" CREATED="1397224118466" MODIFIED="1397224326038"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      DELETE /users/{login}/membership/{id}
    </p>
    <p>
      DELETE /users/{login}/teams/{name}/memberships/{id}
    </p>
  </body>
</html>
</richcontent>
<node TEXT="delete the membership" ID="ID_1943576479" CREATED="1397226059754" MODIFIED="1397226073030"/>
<node TEXT="remove the organization from the user&apos;s list of organizations, if this was the only team of the organization where this user belongs" ID="ID_1217292940" CREATED="1397226143895" MODIFIED="1397226175235"/>
</node>
<node TEXT="CRUD operations for membership" ID="ID_1570620145" CREATED="1397226085234" MODIFIED="1397226092523">
<hook NAME="SummaryNode"/>
</node>
</node>
</node>
<node TEXT="By REST conventions" ID="ID_1479493227" CREATED="1397220613467" MODIFIED="1397220623393">
<node TEXT="GET should not do any changes to resources, like a SELECT" ID="ID_1387011991" CREATED="1397220623771" MODIFIED="1397220640317"/>
<node TEXT="POST creates a resource e.g. /users/{login}/teams creates a new team in the organization with id = login" ID="ID_212346515" CREATED="1397220641045" MODIFIED="1397220686313"/>
<node TEXT="PUT updates a resources e.g. /users/{login} should update the user" ID="ID_68594766" CREATED="1397220687411" MODIFIED="1397220703725"/>
<node TEXT="DELETE deletes a resource" ID="ID_158054307" CREATED="1397220741985" MODIFIED="1397220751343"/>
</node>
</node>
<node TEXT="UI" FOLDED="true" ID="ID_1321636465" CREATED="1397203388168" MODIFIED="1397203389840">
<node TEXT="User View" ID="ID_592079936" CREATED="1397203403088" MODIFIED="1397213054699">
<node TEXT="Profile" ID="ID_1123418567" CREATED="1397203448304" MODIFIED="1397204476982">
<node TEXT="form: fields for name, email etc" ID="ID_22442685" CREATED="1397203406810" MODIFIED="1397204492708"/>
<node TEXT="actions: Update Profile" ID="ID_116059117" CREATED="1397204494706" MODIFIED="1397204501828"/>
</node>
<node TEXT="Settings" ID="ID_171583360" CREATED="1397204506728" MODIFIED="1397204516462">
<node TEXT="change password" ID="ID_53162093" CREATED="1397204516900" MODIFIED="1397204524316"/>
<node TEXT="delete account" ID="ID_540620821" CREATED="1397205401318" MODIFIED="1397205404390"/>
</node>
<node TEXT="Repositories" ID="ID_1305503654" CREATED="1397204544348" MODIFIED="1397204548092">
<node TEXT="list view of repositories" ID="ID_1784098907" CREATED="1397204548710" MODIFIED="1397204558702"/>
<node TEXT="actions" ID="ID_1831694376" CREATED="1397212326093" MODIFIED="1397212328757">
<node TEXT="Create New Repository" ID="ID_262167636" CREATED="1397212329737" MODIFIED="1397212336743"/>
</node>
</node>
<node TEXT="Organizations" ID="ID_1856457661" CREATED="1397204583618" MODIFIED="1397204588950">
<node TEXT="list view of organizations" ID="ID_1153277057" CREATED="1397204589718" MODIFIED="1397222481082"/>
<node TEXT="actions" ID="ID_1098282816" CREATED="1397204765847" MODIFIED="1397204768507">
<node TEXT="Create New Organization" ID="ID_1290493853" CREATED="1397204768509" MODIFIED="1397204776541"/>
</node>
</node>
</node>
<node TEXT="Organization View" ID="ID_559483205" CREATED="1397204907291" MODIFIED="1397213149230">
<node TEXT="Profile" ID="ID_641877159" CREATED="1397205479508" MODIFIED="1397205484760">
<node TEXT="visible only for Owners" ID="ID_275939230" CREATED="1397205485928" MODIFIED="1397205514383"/>
</node>
<node TEXT="Settings" ID="ID_1600792879" CREATED="1397205647015" MODIFIED="1397205650037">
<node TEXT="visible only for Owners" ID="ID_1623579461" CREATED="1397205485928" MODIFIED="1397205514383"/>
</node>
<node TEXT="Repositories" ID="ID_1162706432" CREATED="1397205650925" MODIFIED="1397205655227"/>
<node TEXT="actions" ID="ID_1782790098" CREATED="1397205575635" MODIFIED="1397205590302">
<node TEXT="View Teams" ID="ID_1988093389" CREATED="1397205590963" MODIFIED="1397206693125">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_1283392274" STARTINCLINATION="253;0;" ENDINCLINATION="253;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
<node TEXT="View Members" ID="ID_360041669" CREATED="1397205595523" MODIFIED="1397206884971">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_779441152" STARTINCLINATION="419;0;" ENDINCLINATION="419;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
</node>
</node>
<node TEXT="Teams" ID="ID_1283392274" CREATED="1397205693245" MODIFIED="1397205695327">
<node TEXT="list view of teams (with client-side filtering?)" ID="ID_1291607099" CREATED="1397205697175" MODIFIED="1397206341710"/>
<node TEXT="actions" ID="ID_1284610949" CREATED="1397205752837" MODIFIED="1397205754519">
<node TEXT="New Team" ID="ID_903648015" CREATED="1397205756047" MODIFIED="1397206394248">
<arrowlink SHAPE="CUBIC_CURVE" COLOR="#000000" WIDTH="2" TRANSPARENCY="80" FONT_SIZE="9" FONT_FAMILY="SansSerif" DESTINATION="ID_548209170" STARTINCLINATION="114;0;" ENDINCLINATION="114;0;" STARTARROW="NONE" ENDARROW="DEFAULT"/>
</node>
</node>
</node>
<node TEXT="Add/Edit Team" ID="ID_548209170" CREATED="1397206376986" MODIFIED="1397206380708">
<node TEXT="form: name, description" ID="ID_8429237" CREATED="1397206431708" MODIFIED="1397206441050"/>
<node TEXT="actions" ID="ID_256337847" CREATED="1397206442048" MODIFIED="1397206444340">
<node TEXT="Create (if add)" ID="ID_1497989922" CREATED="1397206445128" MODIFIED="1397206450764"/>
<node TEXT="Update (if edit)" ID="ID_368012027" CREATED="1397206451452" MODIFIED="1397206456084"/>
<node TEXT="Delete (if edit)" ID="ID_453209098" CREATED="1397206456862" MODIFIED="1397206461366"/>
</node>
</node>
<node TEXT="Team View" ID="ID_173497492" CREATED="1397206469784" MODIFIED="1397206473480">
<node TEXT="list view of members (with client-side filtering?)" ID="ID_1330822438" CREATED="1397206492444" MODIFIED="1397206518188"/>
<node TEXT="actions: Add Member" ID="ID_1762300531" CREATED="1397206553906" MODIFIED="1397206561608">
<node TEXT="opens a pop-up to search by login" ID="ID_1236841425" CREATED="1397206571826" MODIFIED="1397206580712"/>
</node>
</node>
<node TEXT="Members View" ID="ID_779441152" CREATED="1397206875615" MODIFIED="1397206880281">
<node TEXT="list view of members (with client-side filtering?)" ID="ID_349318360" CREATED="1397206944271" MODIFIED="1397206958914">
<node TEXT="Login             Teams               Status&#xa;&gt; john            Owners              Owner&#xa;&gt; jane            Owners, RW 1  Owner&#xa;&gt; joe              RW 2                   Member" ID="ID_357757680" CREATED="1397207850319" MODIFIED="1397208057550"/>
</node>
<node TEXT="actions" ID="ID_1094391684" CREATED="1397207930673" MODIFIED="1397207934205">
<node TEXT="Add Member" ID="ID_1730272800" CREATED="1397207935713" MODIFIED="1397207946387">
<node TEXT="opens a pop-up to search by login" ID="ID_1215304584" CREATED="1397207947535" MODIFIED="1397207963062"/>
</node>
<node TEXT="Remove from Organization" ID="ID_92835025" CREATED="1397208261532" MODIFIED="1397208272624">
<node TEXT="member renderer with checkboxes, remove selected members" ID="ID_994682577" CREATED="1397208273902" MODIFIED="1397208316838"/>
</node>
</node>
</node>
<node TEXT="Abstract Views" ID="ID_1313831239" CREATED="1397222511442" MODIFIED="1397464562564">
<node TEXT="ListView" ID="ID_140026926" CREATED="1397222518662" MODIFIED="1397222521256">
<node TEXT="for repositories, organizations, teams, members" ID="ID_76784492" CREATED="1397222523264" MODIFIED="1397222593304"/>
<node TEXT="has menu bar with Refresh | Add | Edit | Delete" ID="ID_797205755" CREATED="1397222594322" MODIFIED="1397222657267"/>
<node TEXT="should be easy to add more actions to menu bar" ID="ID_1506447269" CREATED="1397222915051" MODIFIED="1397222924833"/>
</node>
<node TEXT="FormView" ID="ID_158727179" CREATED="1397222824933" MODIFIED="1397222829693">
<node TEXT="for user profile, user settings, add/edit team" ID="ID_1887995883" CREATED="1397222863378" MODIFIED="1397222890902"/>
</node>
</node>
</node>
<node TEXT="HTML embed" ID="ID_33613293" CREATED="1397227555472" MODIFIED="1397227564825">
<node TEXT="1. simple HTML embed in Flex, i.e. everything happens in the same frame" ID="ID_374940524" CREATED="1397227580180" MODIFIED="1397227684511">
<node TEXT="GET means changing the URL of the frame or ajax calls" ID="ID_529941283" CREATED="1397227689880" MODIFIED="1397227730037"/>
<node TEXT="POST/PUT/DELETE are ajax calls" ID="ID_1147016889" CREATED="1397227707577" MODIFIED="1397227725638"/>
<node TEXT="may be better for mobile, where it can get annoying to have too many views" ID="ID_1435759716" CREATED="1397228588567" MODIFIED="1397228606528"/>
</node>
<node TEXT="2. each new view would stay in its own frame embedded in the Flex app" ID="ID_21165331" CREATED="1397227738673" MODIFIED="1397228652686">
<node TEXT="we could have a JS file that is included only for web, that overrides button handlers" ID="ID_1539204693" CREATED="1397228655915" MODIFIED="1397228698340"/>
</node>
<node TEXT="e.g. flow" ID="ID_67688617" CREATED="1397228699329" MODIFIED="1397228749177">
<node TEXT="Flex app: open the Teams list view, i.e. open an EmbedView with a frame with url = /users/{login}/teams" ID="ID_704377517" CREATED="1397228749818" MODIFIED="1397229083497"/>
<node TEXT="HTML: I get the response from the server, and I see the list of teams; click Edit" ID="ID_531956445" CREATED="1397228861742" MODIFIED="1397228904077"/>
<node TEXT="1. go to /users/{login}/teams/{name} in the same frame" ID="ID_648796621" CREATED="1397228968707" MODIFIED="1397229074002"/>
<node TEXT="2. web override; call Flex callback to open the Team view as if opened from Flex" ID="ID_348820834" CREATED="1397229086997" MODIFIED="1397229123553">
<node TEXT="Flex app: open an EmbedView with url = /users/{login}/teams/{name}" ID="ID_1824728223" CREATED="1397229176398" MODIFIED="1397229208551"/>
</node>
</node>
</node>
<node TEXT="Shared cookie for Flex app and embedded HTML" ID="ID_1523070116" CREATED="1397229278761" MODIFIED="1397466056228" LINK="#ID_246775360"/>
<node TEXT="" ID="ID_1534353464" CREATED="1397213173778" MODIFIED="1397213178130">
<icon BUILTIN="help"/>
<node TEXT="pot sa fac log in ca organizatie? ar fi mai bine sa nu, si in cazul asta o org poate sa fie modif doar de Owners" ID="ID_96395930" CREATED="1397213179058" MODIFIED="1397213355212"/>
</node>
<node TEXT="intrebari cs" ID="ID_305634348" CREATED="1397481320049" MODIFIED="1397547378813">
<icon BUILTIN="help"/>
<node TEXT="cum stocam datelein BD? ar merge sa fie stocate relational" ID="ID_696551125" CREATED="1397481326119" MODIFIED="1397481365200"/>
<node TEXT="cum stocam link-urile intre entitiati?" ID="ID_1647830498" CREATED="1397568732783" MODIFIED="1397568741705"/>
<node TEXT="dc. organizatie: am putea sa ne folosim de flag, ca sa returnam un nod de tip &quot;organization&quot;; dar cred ca ne complicam astfel" ID="ID_618301284" CREATED="1397481369509" MODIFIED="1397481491610"/>
<node TEXT="cum e in github" ID="ID_985260703" CREATED="1397481616239" MODIFIED="1397481624830">
<node TEXT="apartenenta la organizatie: doar prin intermediul echipei?" ID="ID_362013118" CREATED="1397481576379" MODIFIED="1397481585700"/>
<node TEXT="apartenenta la un membru: si acolo sunt echipe?" ID="ID_1380091259" CREATED="1397481587099" MODIFIED="1397481615920"/>
</node>
<node TEXT="copii categorisibili?" ID="ID_1713544995" CREATED="1397482188790" MODIFIED="1397482344342"/>
<node TEXT="in github, nu exista join; totul se face din partea ownerului&#xa;update: se pare ca exista un &quot;join&quot;, dar in cadrul echipelor" ID="ID_1585349196" CREATED="1397487769414" MODIFIED="1397487830364"/>
<node TEXT="facem un serviciu nou pentru CRUD? sau folosim node service" ID="ID_1561249654" CREATED="1397489014429" MODIFIED="1397489055606"/>
<node TEXT="cum facem securizarea? adica un user obisnuit: ce il determina sa nu aiba acces la toti userii, sau sa nu primeasca parola? In rest, am fi interzis accesul la acel url pe baza unui rol" ID="ID_95508774" CREATED="1397489827308" MODIFIED="1397489883220"/>
<node TEXT="de folosit &quot;salt&quot;" ID="ID_935671707" CREATED="1397568899144" MODIFIED="1397568903226"/>
<node TEXT="cum facem sa nu trimitem pe clinet parola hashed?" ID="ID_1976762061" CREATED="1397490101674" MODIFIED="1397490118336">
<node TEXT="de fapt la logare, avem nevoie de el, ca sa comparam" ID="ID_1536335916" CREATED="1397568876804" MODIFIED="1397568891866"/>
</node>
</node>
</node>
</node>
<node TEXT="GH194 - Analysis: permissions system" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1630631950" CREATED="1395327008207" MODIFIED="1396438625774" LINK="https://github.com/flower-platform/flower-platform-4/issues/194">
<node TEXT="Simple mechanism for &quot;Community&quot; version" ID="ID_12447612" CREATED="1395327013467" MODIFIED="1395327028679">
<node TEXT="Everything read-only for everybody" ID="ID_1391625436" CREATED="1395330328836" MODIFIED="1395330391208"/>
<node TEXT="Restrict write operations on r/o resources (besides save)" ID="ID_1207344318" CREATED="1395330342176" MODIFIED="1395330512858">
<node TEXT="Because somebody without rights could modify (but not save). And somebody else with write rights, could save, writing the former modifs as well." ID="ID_1357756609" CREATED="1395330436946" MODIFIED="1395330484198"/>
</node>
</node>
<node TEXT="More complex, for &quot;Pro&quot; version, cf. what we had in FP2" ID="ID_231240463" CREATED="1395327029037" MODIFIED="1395328170774">
<node TEXT="We could even have in mind dual path: repo + path (i.e. resource level). And path within resource as well." ID="ID_6461757" CREATED="1395330516466" MODIFIED="1395330553098"/>
</node>
</node>
</node>
<node TEXT="util" STYLE_REF="In progress (Yellow)" FOLDED="true" POSITION="right" ID="ID_313601750" CREATED="1391693592836" MODIFIED="1392717164438">
<icon BUILTIN="messagebox_warning"/>
<node TEXT="Limitations" STYLE_REF="New (Red)" ID="ID_522987500" CREATED="1392636667931" MODIFIED="1392636687373">
<icon BUILTIN="messagebox_warning"/>
<font BOLD="true"/>
<node TEXT="Dynamic category based on node type (and not node) will generate issues when we&apos;ll have dual resource: BD and file" ID="ID_1839662731" CREATED="1392717064296" MODIFIED="1392717106728">
<node TEXT="The dynamic category system is based on node type, not node. Because performance. This will not be good when we&apos;ll have 2 types of resources, for the same node: server/bd, server/file (for import). My idea: copy the whole typeDescRegistry, and append resource type. We could provide them at the beginning, without sorting. But not good because of Update recorder." ID="ID_1770760997" CREATED="1392636689313" MODIFIED="1392636884074"/>
<node TEXT="We could have another solution: have: getAdditiveController(..., useCachedDynamic...); when false, queries the real thing. And for the controllers that might be provided per resource, call with false. Maybe even have a descriptors, which says which controller type shouldn&apos;t use the cached value." ID="ID_1827298674" CREATED="1392799169566" MODIFIED="1392799303737"/>
</node>
</node>
<node TEXT="Controller and Type Descriptor" STYLE_REF="Validated (Green)" ID="ID_1020611036" CREATED="1391693603365" MODIFIED="1391777276593"><richcontent TYPE="DETAILS">

<html>
  <head>
    
  </head>
  <body>
    <p>
      Registry that knows, for a node type, which controllers are configured (via a Type Descriptor per type).
    </p>
    <p>
      Category mechanism: types that belong to a category, inherit the controllers contributed for that category as well.
    </p>
    <p>
      Order management. Dynamic categories.
    </p>
  </body>
</html>
</richcontent>
<node TEXT="org.flowerplatform.util.controller" STYLE_REF="Code" ID="ID_190032722" CREATED="1391693802157" MODIFIED="1391693809338"/>
</node>
<node TEXT="Servlets" STYLE_REF="In progress (Yellow)" ID="ID_1389531373" CREATED="1392109474456" MODIFIED="1392389753074">
<node TEXT="Common behavior" STYLE_REF="In progress (Yellow)" ID="ID_1079547779" CREATED="1392389170525" MODIFIED="1392389739183">
<node TEXT="If not found =&gt; return error 404" STYLE_REF="Done (Blue)" ID="ID_696928220" CREATED="1392389178840" MODIFIED="1392389749756"/>
<node TEXT="Use a temporary dir" STYLE_REF="In progress (Yellow)" ID="ID_1838904160" CREATED="1392389199401" MODIFIED="1392389737737">
<node TEXT="If the resource has already been generated (and exists in the temp dir) =&gt; serve it form there. Otherwise, generate it" ID="ID_1031299783" CREATED="1392389235256" MODIFIED="1392389305891"/>
<node TEXT="On start, depending on a property, delete cache" ID="ID_1786570001" CREATED="1392389361979" MODIFIED="1392389371338"/>
<node TEXT="GH63 - PublicResourcesServlet and ImageComposerServlet: Use a temporary dir for cache" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1677303535" CREATED="1392389199401" MODIFIED="1392389435598" LINK="https://github.com/flower-platform/flower-platform-4/issues/63">
<node TEXT="Redmine" ID="ID_519379139" CREATED="1392389441940" MODIFIED="1392389441941">
<node TEXT="RM9016 - GH63 - PublicResourcesServlet and ImageComposerServlet: Use a temporary dir for cache" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1349507" CREATED="1392389441942" MODIFIED="1392389441945" LINK="http://csp1/redmine/issues/9016"/>
<node TEXT="RM9017 - [Review] GH63 - PublicResourcesServlet and ImageComposerServlet: Use a temporary dir for cache" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1427524935" CREATED="1392389442264" MODIFIED="1392389442270" LINK="http://csp1/redmine/issues/9017"/>
</node>
<node TEXT="Expected results" ID="ID_1687529904" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
</node>
<node TEXT="Construction site" ID="ID_301599046" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="In web, se foloseste dir temp tomcat" ID="ID_1053334191" CREATED="1392389478933" MODIFIED="1392389487343"/>
<node TEXT="In eclipse ce am putea folosi? are si jetyy temp? sau temp system? sau are eclipse un temp?" ID="ID_21931758" CREATED="1392389487576" MODIFIED="1392389506260"/>
<node TEXT="probabil ca se va muta in ResourcesServlet logica din PuReSer care serveste un fisier; caci e folosita daca fisierul e gasit in temp" ID="ID_549737971" CREATED="1392389509950" MODIFIED="1392389542439"/>
<node TEXT="si pentru cazul image composer; si publ res servlet" ID="ID_254081491" CREATED="1392389554698" MODIFIED="1392389573931"/>
<node TEXT="probabil se va schimba separatorul din | in #" ID="ID_1706330024" CREATED="1392389643141" MODIFIED="1392389655704"/>
</node>
</node>
</node>
</node>
<node TEXT="PublicResourcesServlet" STYLE_REF="Done (Blue)" ID="ID_1641284814" CREATED="1392109477568" MODIFIED="1392389742752"/>
<node TEXT="ImageComposerServlet" STYLE_REF="Done (Blue)" ID="ID_439023993" CREATED="1392109485628" MODIFIED="1392389741822">
<node TEXT="GH43 - ImageComposerServlet" STYLE_REF="Roadmap node (Light yellow cloud)" FOLDED="true" ID="ID_195226825" CREATED="1392109485628" MODIFIED="1392717141168" LINK="https://github.com/flower-platform/flower-platform-4/issues/43">
<icon BUILTIN="flag-green"/>
<node TEXT="Expected results" ID="ID_1162624753" CREATED="1389360413465" MODIFIED="1389384758648">
<icon BUILTIN="revision"/>
</node>
<node TEXT="Construction site" ID="ID_1073555325" CREATED="1389360401598" MODIFIED="1389384772869">
<icon BUILTIN="mindmap"/>
<node TEXT="Mutare in .util; inregistrare din .core" ID="ID_1020702848" CREATED="1392109540126" MODIFIED="1392109553335"/>
<node TEXT="Doc" ID="ID_1073335883" CREATED="1392109561800" MODIFIED="1392109563198"/>
<node TEXT="ImageComp: sa nu extinda PublicRes; eventual o clasa comuna intre ele pentru acel 404" ID="ID_300583709" CREATED="1392109569488" MODIFIED="1392109589989"/>
<node TEXT="Flex:" ID="ID_16320126" CREATED="1392109593021" MODIFIED="1392109596582">
<node TEXT="getImageComposerUrl(...)" ID="ID_60005623" CREATED="1392109693635" MODIFIED="1392109732138">
<node TEXT="dc nu ex prefix, se adauga prefix" ID="ID_642370302" CREATED="1392109748370" MODIFIED="1392109755861"/>
</node>
</node>
<node TEXT="dir: sync-markers/..." ID="ID_1797882987" CREATED="1392109821212" MODIFIED="1392109835008"/>
</node>
<node TEXT="Redmine" ID="ID_1546896538" CREATED="1392111568868" MODIFIED="1392111568876">
<node TEXT="RM8971 - GH43 - ImageComposerServlet" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1555530619" CREATED="1392111568878" MODIFIED="1392111568894" LINK="http://csp1/redmine/issues/8971"/>
<node TEXT="RM8972 - [Review] GH43 - ImageComposerServlet" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1317322797" CREATED="1392111569234" MODIFIED="1392111569238" LINK="http://csp1/redmine/issues/8972"/>
</node>
</node>
</node>
</node>
</node>
<node TEXT="web" STYLE_REF="In progress (Yellow)" FOLDED="true" POSITION="right" ID="ID_279414517" CREATED="1394460892267" MODIFIED="1394460974788">
<node TEXT="Download/upload files" STYLE_REF="Done 2 (Light Blue)" ID="ID_598447254" CREATED="1394460896017" MODIFIED="1397745294679">
<node TEXT="GH116 - Download/upload files" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1761286350" CREATED="1394460896017" MODIFIED="1394460910888" LINK="https://github.com/flower-platform/flower-platform-4/issues/116">
<node TEXT="Redmine" ID="ID_20027778" CREATED="1395913200928" MODIFIED="1395913200958">
<node TEXT="RM9219 - GH116 - Download/upload files" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1532196324" CREATED="1395913200958" MODIFIED="1395913200978" LINK="http://csp1/redmine/issues/9219"/>
<node TEXT="RM9220 - [Review] GH116 - Download/upload files" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_621563589" CREATED="1395913201548" MODIFIED="1395913201558" LINK="http://csp1/redmine/issues/9220"/>
</node>
<node TEXT="pare sa nu mearga download pe fisier; merge pe folder" ID="ID_422898135" CREATED="1397650345958" MODIFIED="1397745306026"/>
<node TEXT="ar trebui sa mearga si pe fileSystem" ID="ID_468210826" CREATED="1397651079197" MODIFIED="1397651085281"/>
</node>
</node>
<node TEXT="Repository Management" ID="ID_947187787" CREATED="1394460916736" MODIFIED="1394460925078">
<node TEXT="GH117 - Repository Management" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_642862936" CREATED="1394460916736" MODIFIED="1394460930448" LINK="https://github.com/flower-platform/flower-platform-4/issues/117"/>
<node TEXT="GH197 - Property for workspace location" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1982243327" CREATED="1396527806157" MODIFIED="1396527885335" LINK="https://github.com/flower-platform/flower-platform-4/issues/197">
<node TEXT="relative to webapp/web-inf" ID="ID_1967453074" CREATED="1396527818069" MODIFIED="1396527845202"/>
<node TEXT="default = &quot;workspace&quot;" ID="ID_265392554" CREATED="1396527852316" MODIFIED="1396527859553"/>
<node TEXT="if ! exists, create dir" ID="ID_1015386915" CREATED="1396527861092" MODIFIED="1396527866185"/>
<node TEXT="used temporary for the single repo" ID="ID_1107177645" CREATED="1396527869700" MODIFIED="1396527877017"/>
</node>
</node>
<node TEXT="GH199 - Actions Cleaning" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_234498676" CREATED="1396528331753" MODIFIED="1396528355891" LINK="https://github.com/flower-platform/flower-platform-4/issues/199">
<node TEXT="Redmine" ID="ID_141854927" CREATED="1396863125793" MODIFIED="1396863125800">
<node TEXT="RM9275 - GH199 - Actions Cleaning" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1977102507" CREATED="1396863125800" MODIFIED="1396863125813" LINK="http://csp1/redmine/issues/9275"/>
<node TEXT="RM9276 - [Review] GH199 - Actions Cleaning" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_524453810" CREATED="1396863126272" MODIFIED="1396863126275" LINK="http://csp1/redmine/issues/9276"/>
</node>
<node TEXT="actions -&gt; check order index + visibility (e.g. synchronize is always available?)" ID="ID_1313955164" CREATED="1396505994774" MODIFIED="1396506329415"/>
</node>
<node TEXT="GH200 - Test session / CC" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1713883931" CREATED="1396528441743" MODIFIED="1396528465180" LINK="https://github.com/flower-platform/flower-platform-4/issues/200">
<node TEXT="Redmine" ID="ID_1216660191" CREATED="1396881670076" MODIFIED="1396881670105">
<node TEXT="RM9282 - GH200 - Test session / CC" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1432237497" CREATED="1396881670107" MODIFIED="1396881670134" LINK="http://csp1/redmine/issues/9282"/>
<node TEXT="RM9283 - [Review] GH200 - Test session / CC" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1569067569" CREATED="1396881670457" MODIFIED="1396881670465" LINK="http://csp1/redmine/issues/9283"/>
</node>
</node>
<node TEXT="GH201 - Test session / MG" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1120864361" CREATED="1396528441743" MODIFIED="1396528467097" LINK="https://github.com/flower-platform/flower-platform-4/issues/201"/>
<node TEXT="GH202 - Test session / SS" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1913321756" CREATED="1396528441743" MODIFIED="1396528469253" LINK="https://github.com/flower-platform/flower-platform-4/issues/202">
<node TEXT="Redmine" ID="ID_577308610" CREATED="1396967888885" MODIFIED="1396967888907">
<node TEXT="RM9295 - GH202 - Test session / SS" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_298461361" CREATED="1396967888908" MODIFIED="1396967888932" LINK="http://csp1/redmine/issues/9295"/>
<node TEXT="RM9296 - [Review] GH202 - Test session / SS" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1475489639" CREATED="1396967889287" MODIFIED="1396967889290" LINK="http://csp1/redmine/issues/9296"/>
</node>
</node>
<node TEXT="GH206 - Icon for browser" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_554275859" CREATED="1396529072274" MODIFIED="1396529114015" LINK="https://github.com/flower-platform/flower-platform-4/issues/206">
<node TEXT="Don&apos;t forget to change &quot;loading&quot; image" ID="ID_318512129" CREATED="1396529092347" MODIFIED="1396529110321"/>
<node TEXT="Redmine" ID="ID_1959276267" CREATED="1397046481559" MODIFIED="1397046481559">
<node TEXT="RM9299 - GH206 - Icon for browser" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_309276521" CREATED="1397046481569" MODIFIED="1397046481579" LINK="http://csp1/redmine/issues/9299"/>
<node TEXT="RM9300 - [Review] GH206 - Icon for browser" STYLE_REF="Roadmap node (Light yellow cloud)" ID="ID_1446372027" CREATED="1397046482309" MODIFIED="1397046482319" LINK="http://csp1/redmine/issues/9300"/>
</node>
</node>
</node>
</node>
</map>
