TestFluidUMAP : FluidUnitTest {
	classvar target_normalizedDict_sum;

	*initClass {
		target_normalizedDict_sum = [ 192.55911817725, 204.81015736512 ];

		/* target_normalizedDict = [ ("entry144" -> [ 0.72296966480145, 0.21615530462598 ]), ("entry129" -> [ 0.84861819457654, 0.26660587420219 ]), ("entry117" -> [ 0.3661093363477, 0.89526009969922 ]), ("entry52" -> [ 0.81323960804635, 0.79078689295416 ]), ("entry46" -> [ 0.80284184983794, 0.77734470538139 ]),
  ("entry329" -> [ 0.76222351084762, 0.52655581299991 ]), ("entry206" -> [ 0.730021694022, 0.46198789970742 ]), ("entry168" -> [ 0.24362954011412, 0.91898694688952 ]), ("entry227" -> [ 0.72932368640194, 0.12031692731996 ]), ("entry29" -> [ 0.86779547317407, 0.48344982463874 ]),
  ("entry234" -> [ 0.34164012811011, 0.76720218758692 ]), ("entry391" -> [ 0.4884488148614, 0.3695507528746 ]), ("entry243" -> [ 0.58205520407426, 0.9334010848064 ]), ("entry53" -> [ 0.235733679401, 0.20191274565716 ]), ("entry45" -> [ 0.42093662309599, 0.18502794108827 ]),
  ("entry290" -> [ 0.35371646086521, 0.056867602184475 ]), ("entry31" -> [ 0.67300807429005, 0.65130421249429 ]), ("entry212" -> [ 0.85812566963785, 0.8709307938022 ]), ("entry21" -> [ 0.53027037377649, 0.79297658220234 ]), ("entry356" -> [ 0.83814779021032, 0.2575133915046 ]),
  ("entry224" -> [ 0.05889555519332, 0.81610673113512 ]), ("entry348" -> [ 0.80165743202859, 0.67460011645699 ]), ("entry76" -> [ 0.10356965956314, 0.5455643350615 ]), ("entry394" -> [ 0.76212493074082, 0.2212737390269 ]), ("entry262" -> [ 0.20435530079726, 0.30080865398539 ]),
  ("entry337" -> [ 0.22525423351661, 0.86853971637635 ]), ("entry150" -> [ 0.25623507354822, 0.3941544247059 ]), ("entry137" -> [ 0.610503184279, 0.56227903035629 ]), ("entry268" -> [ 0.71658485317537, 0.092644419991034 ]), ("entry372" -> [ 0.22072271301652, 0.19337402715049 ]),
  ("entry114" -> [ 0.7397003234024, 0.13870130449403 ]), ("entry399" -> [ 0.48102215885259, 0.35932126515165 ]), ("entry235" -> [ 0.26301276387928, 0.17702128418703 ]), ("entry141" -> [ 0.94831313225314, 0.94818201853867 ]), ("entry133" -> [ 0.36718644159229, 0.31395603329826 ]),
  ("entry228" -> [ 0.33407112688596, 0.77799004284678 ]), ("entry247" -> [ 0.80167571040166, 0.68127831668941 ]), ("entry253" -> [ 0.55530902685041, 0.9381787442895 ]), ("entry146" -> [ 0.067076950481593, 0.81804880535478 ]), ("entry97" -> [ 0.24199253579588, 0.26166989187119 ]),
  ("entry138" -> [ 0.19866345011115, 0.52682873776163 ]), ("entry119" -> [ 0.9327524326662, 0.92770083573693 ]), ("entry299" -> [ 0.6436582939074, 0.1745757399211 ]), ("entry169" -> [ 0.6736219958899, 0.28326757823132 ]), ("entry272" -> [ 0.46990702973279, 0.3733161275905 ]),
  ("entry302" -> [ 0.64108601139375, 0.57963317618977 ]), ("entry6" -> [ 0.54921970821928, 0.94081495785893 ]), ("entry33" -> [ 0.42807147982041, 0.90710391399013 ]), ("entry139" -> [ 0.3388346669578, 0.084099241749264 ]), ("entry154" -> [ 0.54394953242144, 0.2246637892502 ]),
  ("entry261" -> [ 0.70039848740406, 0.34024319745841 ]), ("entry48" -> [ 0.18103818066186, 0.60074370108785 ]), ("entry233" -> [ 0.90474189927031, 0.90178662310884 ]), ("entry364" -> [ 0.71893531970413, 0.11590420715217 ]), ("entry180" -> [ 0.82736790074062, 0.50755860113766 ]),
  ("entry282" -> [ 0.50485266472395, 0.84272664893892 ]), ("entry36" -> [ 0.86396646775901, 0.46809572341767 ]), ("entry220" -> [ 0.43938828677319, 0.2129574126224 ]), ("entry248" -> [ 0.22688628532718, 0.5669549751678 ]), ("entry375" -> [ 0.52071391992341, 0.36516076468717 ]),
  ("entry194" -> [ 0.64085647449087, 0.24638378063135 ]), ("entry279" -> [ 0.20030037434887, 0.82761452983115 ]), ("entry102" -> [ 0.83791598485295, 0.83014651601596 ]), ("entry57" -> [ 0.54205384464011, 0.24177412287934 ]), ("entry1" -> [ 0.31939082476441, 0.10960782090601 ]),
  ("entry384" -> [ 0.46535169950302, 0.72383001223047 ]), ("entry140" -> [ 0.74880811373382, 0.46834240991782 ]), ("entry120" -> [ 0.12868074968714, 0.50946225423405 ]), ("entry249" -> [ 0.50647506850875, 0.69826925263997 ]), ("entry376" -> [ 0.67299390985759, 0.24740750055042 ]),
  ("entry16" -> [ 0.26131619196294, 0.56713482071419 ]), ("entry79" -> [ 0.48802431582689, 0.72477781418352 ]), ("entry37" -> [ 0.18266544856096, 0.29442940140663 ]), ("entry142" -> [ 0.46997566121301, 0.70683481761676 ]), ("entry165" -> [ 0.78056278754276, 0.21979022162087 ]),
  ("entry256" -> [ 0.8649570250354, 0.45704835248974 ]), ("entry38" -> [ 0.54541880562944, 0.94296443840474 ]), ("entry204" -> [ 0.31220114368046, 0.434365449767 ]), ("entry9" -> [ 0.79400995899338, 0.80927040743731 ]), ("entry223" -> [ 0.84721458151568, 0.5010160179681 ]),
  ("entry161" -> [ 0.098803572413681, 0.54340955086941 ]), ("entry130" -> [ 0.65156678128535, 0.2763587808411 ]), ("entry72" -> [ 0.40705721524649, 0.38067290642098 ]), ("entry236" -> [ 0.12725635789848, 0.5469237778397 ]), ("entry153" -> [ 0.84839813860779, 0.3713997126506 ]),
  ("entry266" -> [ 0.63717059962598, 0.15965089543359 ]), ("entry47" -> [ 0.24310057960367, 0.17692111906358 ]), ("entry278" -> [ 0.62747752930625, 0.1309377260117 ]), ("entry209" -> [ 0.4661757428198, 0.81431835958252 ]), ("entry374" -> [ 0.30367408764283, 0.55554425540028 ]),
  ("entry213" -> [ 0.78617516981918, 0.39402326939595 ]), ("entry314" -> [ 0.65567784246095, 0.60601857598762 ]), ("entry373" -> [ 0.13357991304046, 0.61294456511087 ]), ("entry93" -> [ 0.75808215570762, 0.55287688513677 ]), ("entry377" -> [ 0.91129261279183, 0.90327331272611 ]),
  ("entry395" -> [ 0.63935634308913, 0.5895455362857 ]), ("entry89" -> [ 0.3897988341926, 0.19036446202618 ]), ("entry109" -> [ 0.11496782392789, 0.53201435084818 ]), ("entry28" -> [ 0.90859711784939, 0.8943406732448 ]), ("entry316" -> [ 0.33653264681992, 0.79590249014909 ]),
  ("entry73" -> [ 0.58209039558037, 0.29766339148666 ]), ("entry359" -> [ 0.63504909456089, 0.5555159733733 ]), ("entry179" -> [ 0.62671009983501, 0.57120110640966 ]), ("entry210" -> [ 0.26646717012028, 0.15678841003933 ]), ("entry80" -> [ 0.8480161187895, 0.28831723290698 ]),
  ("entry71" -> [ 0.77630963255595, 0.5241186036027 ]), ("entry328" -> [ 0.76098614060034, 0.54754956845278 ]), ("entry95" -> [ 0.21770961217682, 0.39879920593602 ]), ("entry291" -> [ 0.67717150119998, 0.24546930311897 ]), ("entry77" -> [ 0.11721574099874, 0.59830723243784 ]),
  ("entry44" -> [ 0.3615580886784, 0.6293417203409 ]), ("entry341" -> [ 0.73447411977622, 0.13214120535321 ]), ("entry172" -> [ 0.76175173978785, 0.18199382334609 ]), ("entry239" -> [ 0.19444172358601, 0.92040785355779 ]), ("entry230" -> [ 0.40109016088282, 0.34179874113833 ]),
  ("entry69" -> [ 0.11617183094725, 0.29285817437519 ]), ("entry335" -> [ 0.43854596739575, 0.37793347162673 ]), ("entry379" -> [ 0.44608755191958, 0.92025458281214 ]), ("entry3" -> [ 0.42173251327848, 0.26070110225461 ]), ("entry265" -> [ 0.1460766678553, 0.25022302109014 ]),
  ("entry152" -> [ 0.15262995633156, 0.26057795743578 ]), ("entry58" -> [ 0.32799420087531, 0.76379776451188 ]), ("entry333" -> [ 0.58043169996762, 0.14738768790094 ]), ("entry131" -> [ 0.72255924138379, 0.43507179764679 ]), ("entry252" -> [ 0.50885700403541, 0.70235830641722 ]),
  ("entry242" -> [ 0.4536455903942, 0.21852568766876 ]), ("entry357" -> [ 0.8521768396591, 0.83127119315659 ]), ("entry84" -> [ 0.34996382887195, 0.90167751758603 ]), ("entry207" -> [ 0.32889617717063, 0.7750507223129 ]), ("entry318" -> [ 0.91606428765713, 0.91357534724252 ]),
  ("entry174" -> [ 0.73476188827859, 0.36824736291561 ]), ("entry91" -> [ 0.46108285721657, 0.87886916937153 ]), ("entry311" -> [ 0.82813734263362, 0.87539596110943 ]), ("entry267" -> [ 0.81186618468291, 0.22355583246241 ]), ("entry13" -> [ 0.5137685503501, 0.35055158425981 ]),
  ("entry63" -> [ 0.75369593182486, 0.21959548822446 ]), ("entry246" -> [ 0.48474259736161, 0.72638382125843 ]), ("entry7" -> [ 0.41542799384795, 0.6779842646947 ]), ("entry349" -> [ 0.30969651082438, 0.8972567961993 ]), ("entry354" -> [ 0.14843387705625, 0.50339186507669 ]),
  ("entry181" -> [ 0.092619642901823, 0.7723632780048 ]), ("entry336" -> [ 0.94999998807907, 0.94999998807907 ]), ("entry300" -> [ 0.76053995464108, 0.50368532341592 ]), ("entry301" -> [ 0.22538478080114, 0.79634243455139 ]), ("entry118" -> [ 0.17064568437167, 0.41729327471836 ]),
  ("entry122" -> [ 0.20420728788109, 0.42299467232716 ]), ("entry259" -> [ 0.20369983008599, 0.67335868521282 ]), ("entry258" -> [ 0.19509152527226, 0.83350025422359 ]), ("entry383" -> [ 0.23553660669164, 0.10385000307275 ]), ("entry284" -> [ 0.82844320468958, 0.2400089212826 ]),
  ("entry351" -> [ 0.78847738917748, 0.70932999531617 ]), ("entry251" -> [ 0.8037143439057, 0.20650387462433 ]), ("entry319" -> [ 0.42905969984436, 0.89718766403879 ]), ("entry193" -> [ 0.38822583340636, 0.19352770744702 ]), ("entry75" -> [ 0.67866806781467, 0.35327452881903 ]),
  ("entry380" -> [ 0.32520174954288, 0.43181220352916 ]), ("entry186" -> [ 0.68215899786978, 0.27072381314886 ]), ("entry369" -> [ 0.22708397924325, 0.22726361679082 ]), ("entry104" -> [ 0.58992875725621, 0.26169134307128 ]), ("entry296" -> [ 0.64436235541403, 0.64119742108632 ]),
  ("entry10" -> [ 0.64492154659043, 0.5789293050007 ]), ("entry346" -> [ 0.81116150983902, 0.47922711984646 ]), ("entry286" -> [ 0.79428863005194, 0.44311096040012 ]), ("entry99" -> [ 0.15085026819909, 0.55740639233711 ]), ("entry389" -> [ 0.2143040129094, 0.21149841216862 ]),
  ("entry322" -> [ 0.85642660045736, 0.83749276638771 ]), ("entry353" -> [ 0.63497230783366, 0.23014139998004 ]), ("entry283" -> [ 0.24479600241619, 0.23576051141434 ]), ("entry171" -> [ 0.34840883545672, 0.40972648184668 ]), ("entry347" -> [ 0.84752299016669, 0.2618914350031 ]),
  ("entry15" -> [ 0.7505773830859, 0.1802195938154 ]), ("entry170" -> [ 0.203639496575, 0.88497648440166 ]), ("entry121" -> [ 0.46166101888818, 0.3426490053225 ]), ("entry56" -> [ 0.84351530529363, 0.29108137144785 ]), ("entry345" -> [ 0.11415398825812, 0.29809381576528 ]),
  ("entry363" -> [ 0.38868735388359, 0.64543545069621 ]), ("entry27" -> [ 0.15587566795333, 0.61420061114927 ]), ("entry164" -> [ 0.20844178548083, 0.9232423022082 ]), ("entry195" -> [ 0.21918169949079, 0.35829434886051 ]), ("entry2" -> [ 0.45037048856437, 0.85182949618368 ]),
  ("entry271" -> [ 0.60531681325342, 0.57615958893984 ]), ("entry313" -> [ 0.21134309993122, 0.58785294613168 ]), ("entry361" -> [ 0.42024272253067, 0.9158069170379 ]), ("entry270" -> [ 0.22122462069065, 0.73738675686693 ]), ("entry110" -> [ 0.34259301449763, 0.050000000745058 ]),
  ("entry281" -> [ 0.83026532176162, 0.46748298089102 ]), ("entry123" -> [ 0.5583622752241, 0.9425016026842 ]), ("entry30" -> [ 0.80014982480982, 0.41129036310635 ]), ("entry306" -> [ 0.78557616036996, 0.54460080204736 ]), ("entry173" -> [ 0.058106948385945, 0.82776994660511 ]),
  ("entry226" -> [ 0.094703704540222, 0.55888224047122 ]), ("entry203" -> [ 0.3545058909209, 0.3943813582234 ]), ("entry103" -> [ 0.31476091877041, 0.88591764169571 ]), ("entry378" -> [ 0.83650026298002, 0.25314874051342 ]), ("entry205" -> [ 0.72691830089145, 0.37865559483821 ]),
  ("entry340" -> [ 0.36661795948498, 0.32481999942924 ]), ("entry183" -> [ 0.12297048689319, 0.6203419030987 ]), ("entry197" -> [ 0.54382126815179, 0.32898051604083 ]), ("entry70" -> [ 0.46548918163699, 0.80172008129266 ]), ("entry65" -> [ 0.48691864116375, 0.86136279513903 ]),
  ("entry113" -> [ 0.30701226285954, 0.55687700167751 ]), ("entry338" -> [ 0.21789771627026, 0.20888597903165 ]), ("entry305" -> [ 0.078110268338831, 0.83897850433617 ]), ("entry196" -> [ 0.21603187270236, 0.28468486521619 ]), ("entry231" -> [ 0.21621102723136, 0.84915934767176 ]),
  ("entry273" -> [ 0.91668580878536, 0.91566281781531 ]), ("entry327" -> [ 0.081454780282775, 0.78572499972418 ]), ("entry108" -> [ 0.71823215938083, 0.37233839394503 ]), ("entry219" -> [ 0.82750654454416, 0.82558158296359 ]), ("entry244" -> [ 0.3695886844705, 0.20833757709839 ]),
  ("entry82" -> [ 0.16171415856782, 0.41614823465645 ]), ("entry332" -> [ 0.67677380061149, 0.33606086158993 ]), ("entry185" -> [ 0.20964428454947, 0.84473075834664 ]), ("entry20" -> [ 0.91003062680219, 0.90022094942465 ]), ("entry41" -> [ 0.25076712572726, 0.083131945152582 ]),
  ("entry176" -> [ 0.33487415812041, 0.090928959614145 ]), ("entry371" -> [ 0.21352125958861, 0.35844631315791 ]), ("entry145" -> [ 0.28107410891153, 0.1440903750554 ]), ("entry269" -> [ 0.37049154654769, 0.34394192745814 ]), ("entry202" -> [ 0.47566738528761, 0.80589144894163 ]),
  ("entry8" -> [ 0.43163133806808, 0.24405487121282 ]), ("entry49" -> [ 0.76679318756584, 0.69040231252469 ]), ("entry68" -> [ 0.12869886038127, 0.28406335148149 ]), ("entry166" -> [ 0.5430742101981, 0.93756498746401 ]), ("entry221" -> [ 0.4570149116765, 0.91929636017117 ]),
  ("entry277" -> [ 0.62527151027119, 0.21168243169787 ]), ("entry22" -> [ 0.82945264325636, 0.65252130488647 ]), ("entry191" -> [ 0.83070659579772, 0.39718655211151 ]), ("entry78" -> [ 0.54692548709514, 0.23002601738536 ]), ("entry149" -> [ 0.19166124729222, 0.64731937257458 ]),
  ("entry330" -> [ 0.39560820922152, 0.34379682627607 ]), ("entry182" -> [ 0.79683806173716, 0.54255686012951 ]), ("entry94" -> [ 0.35993855642819, 0.63691621145661 ]), ("entry115" -> [ 0.54949134788833, 0.1705652592036 ]), ("entry62" -> [ 0.70916944402473, 0.11694423804985 ]),
  ("entry12" -> [ 0.63244063722861, 0.18342085412702 ]), ("entry229" -> [ 0.70910244972518, 0.10122595541376 ]), ("entry136" -> [ 0.67048260738819, 0.63561331569051 ]), ("entry40" -> [ 0.20890358765265, 0.31564921824096 ]), ("entry367" -> [ 0.71807228840864, 0.47202977266474 ]),
  ("entry312" -> [ 0.44950071755493, 0.89990507899488 ]), ("entry222" -> [ 0.58939281812491, 0.12983917609033 ]), ("entry64" -> [ 0.49097701594753, 0.9280218780396 ]), ("entry24" -> [ 0.088071955980883, 0.79470066608903 ]), ("entry159" -> [ 0.24136532830644, 0.09036800994328 ]),
  ("entry39" -> [ 0.33659135484304, 0.39758872076026 ]), ("entry344" -> [ 0.60219515156012, 0.56903795229381 ]), ("entry366" -> [ 0.71764846198413, 0.51756413884855 ]), ("entry167" -> [ 0.68387809886516, 0.33535624050774 ]), ("entry96" -> [ 0.34127407746116, 0.41402287949677 ]),
  ("entry360" -> [ 0.8435491136051, 0.8796959814481 ]), ("entry126" -> [ 0.4618986896853, 0.20880750257952 ]), ("entry23" -> [ 0.30978220631495, 0.75409672747516 ]), ("entry325" -> [ 0.80569723050026, 0.40001096121399 ]), ("entry292" -> [ 0.18519412061722, 0.4043956954621 ]),
  ("entry148" -> [ 0.080048394813408, 0.7717735691682 ]), ("entry26" -> [ 0.34595546825453, 0.067392998295792 ]), ("entry387" -> [ 0.44352758023881, 0.35847484426523 ]), ("entry155" -> [ 0.67301236853115, 0.57643978482482 ]), ("entry386" -> [ 0.35360198294674, 0.055093649241542 ]),
  ("entry289" -> [ 0.5330926430679, 0.80892928448849 ]), ("entry309" -> [ 0.79522558422794, 0.50556930677599 ]), ("entry308" -> [ 0.465354203598, 0.84505366841213 ]), ("entry98" -> [ 0.19640736067732, 0.91192830163384 ]), ("entry151" -> [ 0.37036995540968, 0.64195805207842 ]),
  ("entry190" -> [ 0.091065122289818, 0.78059350570489 ]), ("entry59" -> [ 0.2170794157539, 0.4029029556324 ]), ("entry200" -> [ 0.49490898645944, 0.7139997851814 ]), ("entry393" -> [ 0.79994882814874, 0.48850730961904 ]), ("entry55" -> [ 0.27399974186583, 0.55019466048951 ]),
  ("entry382" -> [ 0.11200303651052, 0.3136739418984 ]), ("entry225" -> [ 0.33693024627099, 0.8691811864462 ]), ("entry240" -> [ 0.78709234501868, 0.69346352984138 ]), ("entry156" -> [ 0.35972915273492, 0.063996098909564 ]), ("entry326" -> [ 0.26074625441394, 0.89315030249891 ]),
  ("entry390" -> [ 0.24703633621438, 0.51746773557184 ]), ("entry158" -> [ 0.92032845613689, 0.91001935197647 ]), ("entry54" -> [ 0.94264246266818, 0.94510849033525 ]), ("entry217" -> [ 0.33462065699678, 0.081374829394401 ]), ("entry43" -> [ 0.2363683245056, 0.28911946724495 ]),
  ("entry342" -> [ 0.82844308947092, 0.39390075043695 ]), ("entry315" -> [ 0.87845161350628, 0.87562717259858 ]), ("entry274" -> [ 0.079364402008426, 0.81975038620713 ]), ("entry25" -> [ 0.12277711607316, 0.29500121325131 ]), ("entry368" -> [ 0.56097731884618, 0.31881233562333 ]),
  ("entry128" -> [ 0.44826250190339, 0.23930129871712 ]), ("entry132" -> [ 0.75668209687178, 0.14036102653977 ]), ("entry85" -> [ 0.11078174114807, 0.30711417525697 ]), ("entry86" -> [ 0.81525466262842, 0.82500679811898 ]), ("entry388" -> [ 0.86492548990983, 0.45130317133171 ]),
  ("entry134" -> [ 0.70167033795469, 0.24018448787485 ]), ("entry370" -> [ 0.71176394093156, 0.13211052276239 ]), ("entry42" -> [ 0.526520382876, 0.75660656564103 ]), ("entry106" -> [ 0.11188804768093, 0.54354712026444 ]), ("entry92" -> [ 0.53718174634428, 0.79640199808106 ]),
  ("entry90" -> [ 0.72700286234176, 0.16768323419767 ]), ("entry287" -> [ 0.83538395966701, 0.86955503066385 ]), ("entry101" -> [ 0.6104789448211, 0.11767405947446 ]), ("entry147" -> [ 0.2421782820008, 0.091983753159883 ]), ("entry307" -> [ 0.58267993557208, 0.25054715840348 ]),
  ("entry293" -> [ 0.72249762439841, 0.55337774915952 ]), ("entry35" -> [ 0.72711317626519, 0.11437695243694 ]), ("entry255" -> [ 0.48997170237586, 0.70964805322366 ]), ("entry317" -> [ 0.75670656298599, 0.21885323250411 ]), ("entry135" -> [ 0.17082750373969, 0.43669135731153 ]),
  ("entry187" -> [ 0.05466946742117, 0.83928920039667 ]), ("entry343" -> [ 0.55767076790777, 0.23545745027427 ]), ("entry74" -> [ 0.32879473115837, 0.81968217545841 ]), ("entry320" -> [ 0.33120801851511, 0.7829755707616 ]), ("entry189" -> [ 0.29527686024363, 0.52726279391877 ]),
  ("entry323" -> [ 0.31123158414931, 0.53885614898475 ]), ("entry32" -> [ 0.77153662811304, 0.54308530945836 ]), ("entry208" -> [ 0.20595070105836, 0.9146481812069 ]), ("entry215" -> [ 0.26136182672453, 0.54324782828349 ]), ("entry355" -> [ 0.33225550986564, 0.40772313462348 ]),
  ("entry334" -> [ 0.31650993969447, 0.30063187313354 ]), ("entry111" -> [ 0.39531118901405, 0.92064114764623 ]), ("entry61" -> [ 0.53335411773769, 0.31665904209021 ]), ("entry241" -> [ 0.26412316199915, 0.11176445995312 ]), ("entry397" -> [ 0.21740061973215, 0.71255299459394 ]),
  ("entry178" -> [ 0.36264898153856, 0.19930672515956 ]), ("entry18" -> [ 0.3557773007298, 0.39943289616766 ]), ("entry87" -> [ 0.63353132345176, 0.17633220629232 ]), ("entry51" -> [ 0.84526232668686, 0.81443166929905 ]), ("entry157" -> [ 0.6197368709677, 0.12874518397375 ]),
  ("entry83" -> [ 0.084687920334686, 0.78526958842517 ]), ("entry199" -> [ 0.54857749976059, 0.25489286481953 ]), ("entry124" -> [ 0.79846305631936, 0.65483462751914 ]), ("entry260" -> [ 0.68384934142356, 0.13627030744982 ]), ("entry100" -> [ 0.13577934280272, 0.5077377864081 ]),
  ("entry352" -> [ 0.24398280854438, 0.094900117501133 ]), ("entry19" -> [ 0.19980276355188, 0.91549706133296 ]), ("entry275" -> [ 0.20066330324516, 0.69041830577712 ]), ("entry125" -> [ 0.30092798762836, 0.53715389619831 ]), ("entry365" -> [ 0.78228536169252, 0.21615223175551 ]),
  ("entry218" -> [ 0.83068633620559, 0.89119781429722 ]), ("entry321" -> [ 0.55556450421744, 0.28101649611754 ]), ("entry214" -> [ 0.34731347878527, 0.2131682313968 ]), ("entry297" -> [ 0.3268175257569, 0.4147819749555 ]), ("entry34" -> [ 0.52024860513058, 0.71089889108413 ]),
  ("entry198" -> [ 0.18843159648808, 0.39667007101784 ]), ("entry280" -> [ 0.46403179665206, 0.75429977805978 ]), ("entry264" -> [ 0.79643134927697, 0.46776666155881 ]), ("entry184" -> [ 0.54127301449485, 0.80270316402526 ]), ("entry66" -> [ 0.20967030277083, 0.38011833133372 ]),
  ("entry362" -> [ 0.105153245077, 0.54018464871743 ]), ("entry143" -> [ 0.70206366777897, 0.38818263781136 ]), ("entry112" -> [ 0.39813871846135, 0.19002015169952 ]), ("entry201" -> [ 0.14577207125843, 0.60448960997492 ]), ("entry14" -> [ 0.66840571425971, 0.62364245781413 ]),
  ("entry331" -> [ 0.35397366046943, 0.39542184961668 ]), ("entry285" -> [ 0.19022799387678, 0.65818339211993 ]), ("entry188" -> [ 0.65757630237482, 0.63619550405056 ]), ("entry276" -> [ 0.62391912595559, 0.12028308103731 ]), ("entry245" -> [ 0.20107967635258, 0.68774054959717 ]),
  ("entry350" -> [ 0.93825240510487, 0.93791148654935 ]), ("entry17" -> [ 0.90905788698467, 0.88844788451141 ]), ("entry257" -> [ 0.27335968686742, 0.90448262845723 ]), ("entry304" -> [ 0.27564891350995, 0.92340348576539 ]), ("entry303" -> [ 0.903461762904, 0.89658428040988 ]),
  ("entry232" -> [ 0.4335813006962, 0.38733887518647 ]), ("entry298" -> [ 0.34009196194558, 0.30336222917183 ]), ("entry238" -> [ 0.350163977127, 0.81891226430176 ]), ("entry192" -> [ 0.19137363822414, 0.65118585659934 ]), ("entry237" -> [ 0.39202580382481, 0.6522516256097 ]),
  ("entry162" -> [ 0.79171462988674, 0.71701186784057 ]), ("entry263" -> [ 0.86714367397162, 0.91002462513789 ]), ("entry81" -> [ 0.85338113158694, 0.42769212321674 ]), ("entry175" -> [ 0.11699293557088, 0.30159146362224 ]), ("entry358" -> [ 0.37155840149431, 0.25876578639798 ]),
  ("entry398" -> [ 0.51995023247979, 0.26812155811921 ]), ("entry254" -> [ 0.52415158714876, 0.82318308474771 ]), ("entry295" -> [ 0.54316362440682, 0.93535939989673 ]), ("entry0" -> [ 0.3714008652993, 0.6432864304248 ]), ("entry116" -> [ 0.65894045780105, 0.62430973710944 ]),
  ("entry160" -> [ 0.15222708410622, 0.4472888072226 ]), ("entry88" -> [ 0.71050537765004, 0.44607463448215 ]), ("entry4" -> [ 0.06879415984716, 0.80095965369659 ]), ("entry381" -> [ 0.45298884913535, 0.72479624692487 ]), ("entry211" -> [ 0.23315773511208, 0.86129578828908 ]),
  ("entry324" -> [ 0.31152446352867, 0.57841502937332 ]), ("entry177" -> [ 0.43922704852857, 0.2302535876893 ]), ("entry385" -> [ 0.050000000745058, 0.8205877340069 ]), ("entry396" -> [ 0.28125640123868, 0.13224748604667 ]), ("entry50" -> [ 0.24033774769021, 0.12588114256289 ]),
  ("entry392" -> [ 0.34985641675486, 0.81855276733589 ]), ("entry288" -> [ 0.72117755859377, 0.4406775287433 ]), ("entry127" -> [ 0.36181201154407, 0.20231808529727 ]), ("entry5" -> [ 0.7496621618931, 0.37665852064168 ]), ("entry339" -> [ 0.21310388196158, 0.77395093110475 ]),
  ("entry310" -> [ 0.12446441988796, 0.600393527783 ]), ("entry216" -> [ 0.2629783699447, 0.26741207591661 ]), ("entry250" -> [ 0.34814206715303, 0.64294504669658 ]), ("entry60" -> [ 0.40362250328378, 0.91827711797226 ]), ("entry294" -> [ 0.62216820947995, 0.17875964554568 ]),
	("entry163" -> [ 0.27973443825741, 0.52795509277441 ]), ("entry105" -> [ 0.83418920836435, 0.49686337306592 ]), ("entry107" -> [ 0.62630049803826, 0.20548520806597 ]), ("entry67" -> [ 0.35029483832471, 0.32342959542508 ]), ("entry11" -> [ 0.24653585661328, 0.73381075515811 ]) ].asDict.as(Dictionary)*/
	}

	test_3d_colors {
		var condition = Condition();
		var raw = FluidDataSet(server);
		var standardized = FluidDataSet(server);
		var reduced = FluidDataSet(server);
		var normalized = FluidDataSet(server);
		var standardizer  = FluidStandardize(server);
		var normalizer = FluidNormalize(server, 0.05, 0.95);
		var umap = FluidUMAP(server).numDimensions_(2).numNeighbours_(5).minDist_(0.2).iterations_(50).learnRate_(0.2);
		var colours = Dictionary.newFrom(400.collect{|i|[("entry"++i).asSymbol, 3.collect{1.0.rand}]}.flatten(1));

		var normalizedDict;
		var normalizedDict_sum = [0, 0];

		raw.load(Dictionary.newFrom([\cols, 3, \data, colours]));

		server.sync;

		standardizer.fitTransform(raw,standardized);
		umap.fitTransform(standardized,reduced);
		normalizer.fitTransform(reduced,normalized);

		server.sync;

		normalized.dump { | x |
			normalizedDict = x["data"];
			//Does the summing approach make sense??? Ask PA and Gerard
			normalizedDict.do({ | entry |
				normalizedDict_sum = normalizedDict_sum + entry;
			});
			condition.unhang;
		};

		condition.hang;

		result[\normalizedDictSize] = TestResult(normalizedDict.size, 400);
		result[\normalizedDict] = TestResultEquals(normalizedDict_sum, target_normalizedDict_sum, 40);

		server.sync;
		raw.free;
		standardized.free;
		reduced.free;
		normalized.free;
		standardizer.free;
		normalizer.free;
	}
}