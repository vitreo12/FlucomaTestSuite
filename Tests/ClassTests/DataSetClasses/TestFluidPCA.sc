TestFluidPCA : FluidUnitTest {
	classvar mfcc_dict, reducedarray_target;

	*initClass {
		mfcc_dict = Dictionary[ ("data" -> Dictionary[ ("93" -> [ -28.647018432617, 0.69103521108627, -2.2905869483948, 9.4825229644775, 6.0923643112183, 3.3119711875916, 2.5543704032898, 7.6828327178955, 7.5155692100525, 5.3683681488037, 3.8247404098511, 6.0461626052856 ]), ("46" -> [ -31.425313949585, 2.7866876125336, -0.079253278672695, 8.3774137496948, 4.188060760498, 3.03240275383, 3.5333371162415, 8.0836477279663, 7.1278347969055, 5.3581418991089, 4.0579361915588, 5.6499767303467 ]), ("86" -> [ -18.205352783203, -13.950819015503, 2.5321373939514, 13.19707775116, 4.9528074264526, -2.656712770462, -5.4009313583374, 6.9341769218445, 13.388861656189, -3.1293818950653, -9.9885864257812, 9.106785774231 ]), ("22" -> [ -32.225917816162, 3.4536204338074, 0.58749115467072, 7.8976330757141, 3.4935667514801, 3.5305144786835, 4.7372093200684, 8.7959289550781, 7.1872096061707, 4.910964012146, 3.9417181015015, 5.5051431655884 ]), ("47" -> [ -26.799875259399, 3.541939496994, 0.65961462259293, 7.8207950592041, 3.8727567195892, 3.552216053009, 4.1831378936768, 7.7395315170288, 6.3394708633423, 4.4425296783447, 3.3223152160645, 4.765709400177 ]),
  ("32" -> [ -16.229904174805, -16.482042312622, 1.0394688844681, 12.695567131042, 5.2737379074097, -1.9952952861786, -5.8491654396057, 5.8714690208435, 13.661586761475, -2.4599430561066, -10.37091255188, 8.6851692199707 ]), ("26" -> [ -13.663820266724, -0.40664699673653, 0.75746583938599, 10.671699523926, 4.1538848876953, -0.7867334485054, 0.72958523035049, 7.8760647773743, 6.5870456695557, -0.064816847443581, -1.6752640008926, 4.6696214675903 ]), ("14" -> [ -26.133668899536, -4.5121097564697, -6.958263874054, 10.500473022461, 8.089635848999, 2.2191319465637, 0.69606298208237, 8.4650583267212, 8.9771032333374, 5.2263646125793, 2.617173910141, 5.6833534240723 ]), ("18" -> [ -20.510988235474, -17.178323745728, -1.873960852623, 15.481336593628, 7.7321524620056, -0.3755676150322, -5.5335893630981, 8.0538272857666, 16.260526657104, -2.1498818397522, -10.178614616394, 11.06155872345 ]), ("53" -> [ -23.393280029297, 1.1082639694214, -0.26773503422737, 8.7809505462646, 4.4116311073303, 2.4655032157898, 2.7674162387848, 7.4943232536316, 6.6093697547913, 3.5283212661743, 2.215231180191, 5.7507538795471 ]),
  ("89" -> [ -25.934917449951, 2.4726328849792, -0.000419850286562, 9.8570909500122, 5.8074035644531, 3.7138078212738, 3.9434659481049, 8.553729057312, 7.2255821228027, 4.1985592842102, 2.3038649559021, 4.0564641952515 ]), ("10" -> [ -21.017517089844, -7.5039710998535, -10.187145233154, 11.059154510498, 9.0242767333984, 1.3098171949387, -0.099439278244972, 7.9597883224487, 8.2843008041382, 4.2623157501221, 2.2345879077911, 5.8023247718811 ]), ("23" -> [ -6.3551082611084, -2.193895816803, 0.24350829422474, 10.535948753357, 4.4616150856018, -1.5250836610794, -0.036981113255024, 6.9127731323242, 5.8422951698303, -1.1553822755814, -2.7654173374176, 3.6809496879578 ]), ("55" -> [ -29.682336807251, -0.9955649971962, -0.38597372174263, 12.664712905884, 6.054847240448, 0.66170305013657, 1.7407131195068, 10.558027267456, 10.280786514282, 3.8677771091461, 1.3538984060287, 7.4774875640869 ]), ("12" -> [ -7.642333984375, -3.8932037353516, -6.0689215660095, 5.1755499839783, 3.8826277256012, 0.15162038803101, 0.35506129264832, 4.9397559165955, 4.4777398109436, 2.1291828155518, 1.1760467290878, 3.2152409553528 ]),
  ("57" -> [ -21.290981292725, -1.6862748861313, -2.7197737693787, 5.1080741882324, 2.2354574203491, 1.1253476142883, 2.5881750583649, 6.7112822532654, 5.9028449058533, 4.7046160697937, 4.3670825958252, 5.3163342475891 ]), ("64" -> [ -20.887998580933, 0.029649196192622, -0.071174994111061, 8.259726524353, 3.0341534614563, 0.1550340950489, 1.8363387584686, 7.6281170845032, 6.3786287307739, 3.0177638530731, 1.8371663093567, 4.6481003761292 ]), ("72" -> [ -28.198793411255, -0.59614652395248, -3.4786202907562, 9.8656492233276, 6.6322898864746, 2.8177242279053, 2.1389150619507, 8.4949922561646, 8.6620845794678, 5.6915884017944, 3.7449498176575, 6.0540580749512 ]), ("69" -> [ -18.937793731689, 2.3897085189819, 0.048290058970451, 9.9727640151978, 6.1426577568054, 2.724066734314, 2.3635282516479, 7.0990586280823, 6.1741642951965, 3.1405947208405, 1.4043208360672, 3.9133191108704 ]), ("88" -> [ -30.408563613892, 0.55144566297531, -1.5835765600204, 9.5320482254028, 5.8563041687012, 3.523918390274, 3.5471568107605, 8.6809492111206, 8.3099193572998, 5.4849629402161, 3.1346881389618, 5.1039018630981 ]),
  ("81" -> [ -8.3202610015869, -0.39035314321518, -2.1775274276733, 5.6975555419922, 2.5734055042267, -0.051110491156578, 1.4247875213623, 5.2633624076843, 3.6141178607941, 1.3548645973206, 1.0403091907501, 2.7211196422577 ]), ("28" -> [ -31.35853767395, 3.613597869873, 0.29131507873535, 7.486403465271, 3.2015314102173, 3.2878286838531, 4.1315383911133, 8.1606760025024, 7.0154919624329, 5.5309343338013, 4.3116545677185, 5.9364442825317 ]), ("62" -> [ -14.389716148376, -10.036502838135, 1.4550623893738, 14.524166107178, 5.5451874732971, -2.2385475635529, -3.6266579627991, 8.0745868682861, 12.136615753174, -3.1395273208618, -8.44398021698, 8.3021841049194 ]), ("82" -> [ -20.456165313721, 0.92863619327545, 0.62307804822922, 9.1145401000977, 3.1076381206512, -0.28587013483047, 1.426092505455, 7.6339163780212, 6.3068795204163, 2.7767040729523, 1.8478783369064, 4.7783584594727 ]), ("27" -> [ -31.395042419434, 2.9223747253418, 0.35099333524704, 9.1676225662231, 4.7907891273499, 3.0901870727539, 3.6325840950012, 8.6560068130493, 8.0393257141113, 5.6939516067505, 3.9583282470703, 6.0113382339478 ]),
  ("67" -> [ -31.148111343384, 3.38516664505, 0.21485023200512, 8.4603719711304, 3.9420394897461, 3.4567382335663, 4.039137840271, 8.2537040710449, 7.2030725479126, 5.6584949493408, 4.4438552856445, 6.0230388641357 ]), ("9" -> [ -21.283432006836, -8.1699342727661, -3.0540871620178, 14.184293746948, 7.556492805481, -1.1801491975784, -2.9547672271729, 8.7907400131226, 11.918411254883, 0.18525263667107, -4.2416167259216, 8.9319801330566 ]), ("44" -> [ -12.346371650696, -6.8054857254028, 1.1859862804413, 14.536202430725, 6.2996768951416, -1.8923184871674, -2.3408668041229, 7.5110454559326, 8.6742868423462, -2.3877780437469, -5.7783484458923, 5.4408807754517 ]), ("17" -> [ -23.243316650391, -17.891796112061, 0.79079639911652, 13.627493858337, 6.2380328178406, -1.1046727895737, -6.6602063179016, 6.4586186408997, 16.178522109985, -2.7541971206665, -12.513848304749, 10.133761405945 ]), ("78" -> [ -22.870403289795, -12.351312637329, 1.1446466445923, 14.041059494019, 4.2278137207031, -3.0187697410583, -4.4408874511719, 8.6632986068726, 13.048835754395, -3.5543851852417, -8.1093292236328, 11.200137138367 ]),
  ("60" -> [ -15.7804479599, -0.0079435659572482, -2.2192869186401, 6.9635000228882, 4.0785665512085, 1.1292855739594, 1.6254042387009, 6.3032655715942, 5.3668007850647, 2.7343544960022, 1.5583319664001, 3.7684910297394 ]), ("75" -> [ -11.025840759277, 0.3927489221096, -0.66731762886047, 7.8850469589233, 3.6517200469971, 0.074801370501518, 1.6672811508179, 6.3324990272522, 4.8728957176208, 1.6090401411057, 0.92326259613037, 4.1404914855957 ]), ("13" -> [ -10.290242195129, -7.8628315925598, -2.0069537162781, 10.705226898193, 4.5829410552979, -2.3616604804993, -1.3505816459656, 6.6830005645752, 6.4935684204102, -0.64725488424301, -1.9847785234451, 4.570629119873 ]), ("96" -> [ -7.0130763053894, -10.175066947937, -5.2598505020142, 2.8466353416443, 0.4768448472023, -2.8177886009216, 0.2189249843359, 5.1419916152954, 4.938796043396, 3.7788100242615, 4.5198521614075, 4.0271801948547 ]), ("74" -> [ -23.646024703979, 1.0598684549332, -1.8459049463272, 9.267186164856, 6.2051100730896, 2.9531133174896, 2.3393490314484, 7.3406462669373, 7.1723785400391, 4.659010887146, 3.1400725841522, 4.9717373847961 ]),
  ("1" -> [ -10.505447387695, -2.1719703674316, -0.99529421329498, 8.6589336395264, 3.7588465213776, -0.61070519685745, 0.43108972907066, 6.1753935813904, 4.7210760116577, 0.2701270878315, -0.50898057222366, 3.8923697471619 ]), ("63" -> [ -23.007389068604, 1.2497910261154, 0.38064563274384, 10.248422622681, 4.6483945846558, 0.88599890470505, 2.3182015419006, 8.4282894134521, 6.9167819023132, 2.8356399536133, 1.2030087709427, 4.223934173584 ]), ("94" -> [ -23.601657867432, 0.55744111537933, -1.493821144104, 5.6714835166931, 2.4454395771027, 1.9418839216232, 3.0019643306732, 6.5368828773499, 5.7251954078674, 4.3694577217102, 3.8115723133087, 5.0403594970703 ]), ("48" -> [ -23.974214553833, -14.31014919281, -0.5069864988327, 16.550191879272, 7.2421793937683, -1.9016314744949, -4.646137714386, 9.07972240448, 15.123287200928, -1.5849672555923, -8.1852235794067, 11.138317108154 ]), ("52" -> [ -30.601587295532, 2.5575342178345, -0.48433068394661, 8.9002771377563, 4.7729344367981, 3.8322737216949, 3.8637948036194, 8.0924291610718, 7.3122410774231, 5.3781685829163, 3.8403623104095, 5.7546157836914 ]),
  ("11" -> [ -26.204015731812, -1.3132668733597, -4.6890554428101, 9.2284727096558, 6.7106595039368, 2.9503901004791, 1.8294234275818, 7.433066368103, 7.5234475135803, 4.9866008758545, 2.9259302616119, 4.938374042511 ]), ("19" -> [ -17.984077453613, -10.979223251343, -2.1264972686768, 14.064415931702, 6.4148569107056, -1.7353308200836, -3.0513384342194, 9.246621131897, 12.457848548889, -1.736127614975, -6.6070427894592, 8.7201528549194 ]), ("25" -> [ -26.685440063477, 3.5079762935638, 0.65581971406937, 8.2138738632202, 3.5985286235809, 2.9200508594513, 4.083634853363, 7.9713635444641, 6.6157007217407, 4.320237159729, 3.0215041637421, 4.9577317237854 ]), ("4" -> [ -23.556699752808, -17.873016357422, 0.70416986942291, 14.541481971741, 6.5410208702087, -1.8253397941589, -7.4472689628601, 6.3648142814636, 16.23829460144, -3.2016561031342, -12.643217086792, 10.578657150269 ]), ("90" -> [ -15.612620353699, 0.77763903141022, -1.8343043327332, 5.9094967842102, 3.0737955570221, 1.3129813671112, 2.4883184432983, 6.2427911758423, 5.0388555526733, 3.0658354759216, 2.1725194454193, 3.4851512908936 ]),
  ("41" -> [ -26.675342559814, -5.0599436759949, -2.1589503288269, 14.077655792236, 8.0829849243164, 1.0577113628387, 0.44275918602943, 10.299787521362, 11.343602180481, 2.2482674121857, -1.2550065517426, 7.5910687446594 ]), ("66" -> [ -26.225973129272, 2.8791143894196, 0.8091304898262, 8.8091907501221, 4.3882131576538, 2.8453838825226, 3.5433168411255, 8.183967590332, 6.6905522346497, 4.2316689491272, 2.9308016300201, 4.9527645111084 ]), ("7" -> [ -23.013269424438, -0.5281480550766, -2.4479360580444, 10.903959274292, 7.1414852142334, 1.8649802207947, 1.4507174491882, 8.177038192749, 8.1078491210938, 3.9554681777954, 1.7284685373306, 4.7589540481567 ]), ("65" -> [ -31.939636230469, 3.6836161613464, 0.90816193819046, 8.7428197860718, 3.9714615345001, 3.2689206600189, 4.0932760238647, 8.4648551940918, 7.3908257484436, 5.3170037269592, 3.9862992763519, 5.7593469619751 ]), ("73" -> [ -20.103841781616, 1.0830367803574, -2.4550924301147, 6.9942955970764, 4.7513990402222, 2.4201457500458, 2.0829129219055, 5.9472699165344, 5.7901172637939, 4.2937359809875, 3.0891749858856, 4.4900732040405 ]),
  ("59" -> [ -30.148416519165, 2.2662160396576, -0.54546678066254, 9.4446582794189, 5.5162200927734, 2.9648544788361, 2.7716827392578, 8.144962310791, 7.88258934021, 5.3589906692505, 3.5949809551239, 5.6390895843506 ]), ("76" -> [ -31.3835105896, 4.1290493011475, 1.4047716856003, 9.0319709777832, 4.1596040725708, 3.3425190448761, 4.0289716720581, 8.3517656326294, 7.0562763214111, 5.3907504081726, 4.4006500244141, 6.3223538398743 ]), ("37" -> [ -14.737090110779, -14.54815196991, 0.44543436169624, 11.854229927063, 4.5946626663208, -1.9556113481522, -4.273485660553, 6.3898196220398, 11.677612304688, -1.9691760540009, -7.3550171852112, 7.1766571998596 ]), ("36" -> [ -25.103828430176, -16.203943252563, 3.1964831352234, 13.627940177917, 6.018238067627, -0.89961892366409, -6.0980134010315, 7.7449507713318, 17.086048126221, -2.4716105461121, -12.234860420227, 10.90991973877 ]), ("30" -> [ -23.295318603516, 0.73463833332062, 2.9453008174896, 13.246036529541, 5.30615234375, -0.041146695613861, 1.1267672777176, 9.379599571228, 8.3000860214233, 1.0929865837097, -0.48738113045692, 6.4330267906189 ]),
  ("24" -> [ -31.197561264038, 3.9253873825073, 1.6959043741226, 9.8317394256592, 4.1942453384399, 3.0274152755737, 4.5191378593445, 9.5431890487671, 7.9680256843567, 5.0662360191345, 3.8261651992798, 6.4764366149902 ]), ("5" -> [ -6.1263103485107, -11.759539604187, -2.3470799922943, 9.2625274658203, 2.663985490799, -4.0968036651611, -2.7755033969879, 5.2525925636292, 5.836473941803, -2.3691165447235, -3.96750831604, 3.806271314621 ]), ("6" -> [ -28.127468109131, -0.7910623550415, -2.87144947052, 10.813726425171, 6.58695936203, 1.9546803236008, 1.5752331018448, 8.69114112854, 8.7575702667236, 4.9907288551331, 2.5604074001312, 5.4045443534851 ]), ("3" -> [ -23.180908203125, -4.7979946136475, -1.5043832063675, 11.120025634766, 6.4109115600586, 1.6652666330338, 0.071795046329498, 7.8435139656067, 9.9352140426636, 2.7985327243805, -0.85851055383682, 6.5933451652527 ]), ("68" -> [ -27.159795761108, 2.927139043808, -1.5367513895035, 7.0174674987793, 3.8923995494843, 3.3408203125, 3.2434873580933, 7.131805896759, 6.6564874649048, 5.6416130065918, 4.4751877784729, 5.845018863678 ]),
  ("99" -> [ -26.836822509766, 2.6728565692902, -0.44126388430595, 7.705828666687, 3.9754951000214, 2.9960708618164, 3.4341330528259, 7.8183436393738, 6.8571019172668, 5.0217823982239, 3.8921883106232, 5.844343662262 ]), ("71" -> [ -10.52442741394, -0.24962963163853, -2.6078963279724, 7.2638034820557, 4.8544278144836, 1.123396396637, 1.074427485466, 5.2964129447937, 4.6656360626221, 2.3638367652893, 1.2496435642242, 3.1114809513092 ]), ("91" -> [ -30.89940071106, 4.6155319213867, 2.025360584259, 9.9622859954834, 4.3790664672852, 3.3962554931641, 4.4452314376831, 9.1891841888428, 7.3534784317017, 4.5360240936279, 3.6813881397247, 6.4162316322327 ]), ("50" -> [ -6.0411238670349, -10.922931671143, -5.5718674659729, 3.5504124164581, -0.028267543762922, -3.613169670105, 0.51633936166763, 6.9924697875977, 6.3601441383362, 3.6269171237946, 3.503408908844, 3.2400305271149 ]), ("77" -> [ -25.515542984009, 1.9232915639877, 0.21491049230099, 8.7116289138794, 4.3427295684814, 2.5132672786713, 3.1697707176208, 8.3152666091919, 7.2684717178345, 3.8515374660492, 2.5262944698334, 6.1048374176025 ]),
  ("38" -> [ -28.428495407104, -1.7259072065353, -1.7882630825043, 12.585540771484, 7.1095986366272, 1.4759271144867, 0.95635312795639, 9.6945991516113, 10.105303764343, 4.0816512107849, 0.84686410427094, 6.0965466499329 ]), ("98" -> [ -17.101528167725, -2.7796630859375, -5.3330006599426, 8.4489040374756, 5.7155404090881, 1.3389179706573, 0.64360535144806, 6.0891399383545, 5.9845480918884, 3.3989474773407, 2.2577991485596, 4.6497592926025 ]), ("97" -> [ -26.46800994873, 3.3636689186096, 1.4968639612198, 12.076892852783, 5.689558506012, 2.1283297538757, 2.4117248058319, 8.6880474090576, 7.2356262207031, 3.6208391189575, 1.6491866111755, 4.9571957588196 ]), ("54" -> [ -23.327602386475, -14.514223098755, -0.029385892674327, 15.414674758911, 5.9207096099854, -1.9276431798935, -5.6955261230469, 8.3019390106201, 15.745294570923, -2.7779610157013, -10.029388427734, 11.683599472046 ]), ("92" -> [ -23.932455062866, 0.0300815962255, -2.2030923366547, 9.3201208114624, 5.6249017715454, 2.5081739425659, 2.3824667930603, 7.7354378700256, 7.5326809883118, 4.1596851348877, 2.1998181343079, 4.989951133728 ]),
  ("33" -> [ -24.516819000244, -2.9972851276398, 2.0987050533295, 14.597719192505, 6.0184988975525, -0.65030169487, -0.51702338457108, 9.5976791381836, 10.448823928833, 0.22714455425739, -3.3672800064087, 6.7502455711365 ]), ("20" -> [ -10.976735115051, -6.402355670929, -0.84201508760452, 9.9445133209229, 2.9054870605469, -3.4426200389862, -1.512514591217, 6.4477634429932, 5.2366189956665, -1.6159118413925, -2.5016441345215, 3.4184591770172 ]), ("45" -> [ -29.920835494995, 0.28447967767715, -1.3205169439316, 10.388661384583, 6.0813980102539, 2.6740119457245, 2.8757655620575, 8.8451156616211, 8.5275382995605, 5.2149457931519, 3.0346717834473, 5.3894829750061 ]), ("31" -> [ -22.577911376953, -11.694165229797, 0.95405876636505, 12.404577255249, 4.7963404655457, -1.8753627538681, -4.9886612892151, 6.4577646255493, 13.010634422302, -2.449723482132, -9.4763956069946, 8.6577415466309 ]), ("29" -> [ -11.901459693909, -0.86474138498306, -1.6052992343903, 5.518247127533, 1.9727438688278, 0.23208796977997, 1.6017752885818, 5.4561667442322, 4.1722855567932, 2.0307695865631, 1.6618676185608, 3.7710001468658 ]),
  ("84" -> [ -29.61110496521, 4.12806224823, 1.0209884643555, 7.5314378738403, 3.2290647029877, 3.1901292800903, 4.2110834121704, 7.8429608345032, 6.2371459007263, 4.6687622070312, 4.2767100334167, 5.9988856315613 ]), ("21" -> [ -32.477127075195, 2.5262131690979, 0.8738300204277, 9.6563272476196, 4.2819471359253, 2.790109872818, 4.0322351455688, 9.4791479110718, 8.5211200714111, 5.4083876609802, 3.3885018825531, 5.4661908149719 ]), ("70" -> [ -27.679971694946, -0.30871114134789, -4.0704874992371, 8.9101800918579, 6.6484436988831, 3.2339181900024, 2.1662817001343, 7.6482996940613, 8.0730323791504, 6.1226406097412, 4.2399392127991, 6.0499758720398 ]), ("39" -> [ -32.272201538086, 2.4911670684814, 0.60312330722809, 9.0560150146484, 3.9228990077972, 2.6357448101044, 3.9676976203918, 9.314341545105, 8.3571376800537, 5.2004776000977, 3.1335070133209, 5.0914897918701 ]), ("80" -> [ -31.689531326294, 4.371365070343, 1.7113497257233, 8.9291372299194, 3.7847783565521, 3.2128126621246, 4.5263514518738, 9.1759634017944, 7.5199599266052, 5.058696269989, 3.8459661006927, 5.7950387001038 ]),
  ("51" -> [ -28.338903427124, 1.727466583252, -0.86727434396744, 10.792944908142, 5.748300075531, 2.4349739551544, 2.3795475959778, 8.6818675994873, 7.9074425697327, 4.3154935836792, 2.5420165061951, 5.4236969947815 ]), ("85" -> [ -26.285974502563, -14.677320480347, 3.4021165370941, 14.544130325317, 5.2762317657471, -2.6010665893555, -5.8405628204346, 8.6884107589722, 16.518989562988, -3.3124575614929, -12.130433082581, 10.739388465881 ]), ("79" -> [ -15.912400245667, -1.5314116477966, 0.82156354188919, 11.68455696106, 4.6391410827637, -0.90432584285736, 0.63710743188858, 8.0885906219482, 7.0836281776428, 1.0792073011398, -0.62271595001221, 4.7930102348328 ]), ("8" -> [ -9.3764505386353, -4.4033131599426, -1.3007899522781, 9.4885063171387, 5.096001625061, -0.45628410577774, -0.87295114994049, 6.2194476127625, 7.0262808799744, -1.0327266454697, -3.7410981655121, 4.2239775657654 ]), ("87" -> [ -28.543436050415, -0.54454183578491, 1.9121150970459, 13.745304107666, 5.9807467460632, 0.5888876914978, 1.0098134279251, 10.449621200562, 10.821193695068, 2.5326528549194, -1.0466462373734, 6.4484024047852 ]),
  ("34" -> [ -31.68035697937, 1.9917379617691, 0.14540980756283, 9.3963890075684, 4.2908411026001, 2.565000295639, 3.5754766464233, 9.0430212020874, 8.11496925354, 5.1493525505066, 2.966649055481, 5.0346260070801 ]), ("61" -> [ -26.207744598389, -14.922407150269, 1.6354019641876, 15.136856079102, 4.8509449958801, -2.7996888160706, -6.2823166847229, 7.8446102142334, 15.820847511292, -3.8749480247498, -11.5662317276, 11.051831245422 ]), ("2" -> [ -29.769636154175, 1.9031164646149, -0.12737268209457, 11.287628173828, 5.8606700897217, 2.2278609275818, 2.3874938488007, 8.8583688735962, 8.3382196426392, 5.008722782135, 3.1172032356262, 6.6063942909241 ]), ("15" -> [ -17.805955886841, -0.7596954703331, -2.5566868782043, 8.8712663650513, 5.47350025177, 1.4616553783417, 1.5620378255844, 7.2784748077393, 6.6690282821655, 2.8140859603882, 1.2380828857422, 4.359263420105 ]), ("35" -> [ -10.756218910217, -8.4224786758423, 1.217128276825, 9.8005638122559, 3.9210312366486, -1.7897545099258, -3.1720871925354, 5.8740248680115, 9.4078569412231, -2.642263174057, -7.7554626464844, 5.6254000663757 ]),
  ("42" -> [ -29.742498397827, 1.003124833107, -0.85631173849106, 10.30726146698, 6.0448508262634, 2.8169116973877, 3.0849871635437, 8.8767528533936, 8.5210380554199, 5.228099822998, 2.9734196662903, 5.128969669342 ]), ("58" -> [ -19.608247756958, -7.0429539680481, -2.9803919792175, 7.3898363113403, 2.8237638473511, -1.756151676178, 0.48202282190323, 8.4243688583374, 8.7670202255249, 5.1224980354309, 4.1579351425171, 5.8087134361267 ]), ("0" -> [ -24.81568145752, 4.2467188835144, -0.42985007166862, 6.1950445175171, 3.4458336830139, 3.9337704181671, 3.6231870651245, 6.3453259468079, 5.5553016662598, 4.5396428108215, 3.2901313304901, 4.81378698349 ]), ("40" -> [ -23.647411346436, -9.3724155426025, 1.8795781135559, 13.688008308411, 5.9076862335205, -0.47816982865334, -3.1138496398926, 8.5222625732422, 13.327958106995, -2.0409305095673, -8.3042812347412, 8.7154941558838 ]), ("43" -> [ -20.066999435425, -4.0837411880493, -0.027257841080427, 10.482166290283, 4.995677947998, 1.2196487188339, 0.66257041692734, 7.7707715034485, 8.6112403869629, 0.99274814128876, -1.7705532312393, 6.2932200431824 ]),
  ("56" -> [ -30.59391784668, 2.1321036815643, -0.40634509921074, 9.2065916061401, 4.6816487312317, 2.8437418937683, 2.9914813041687, 8.2521152496338, 7.83056640625, 5.5583982467651, 3.7074081897736, 5.4921832084656 ]), ("49" -> [ -26.202314376831, 0.84801572561264, -1.964287519455, 10.845952987671, 6.7472167015076, 2.4128665924072, 2.0403687953949, 8.6398591995239, 8.3141069412231, 4.7570462226868, 2.5195167064667, 5.5345764160156 ]), ("95" -> [ -21.960556030273, 0.5356462597847, 0.017781004309654, 10.457324028015, 4.8334913253784, 0.80545926094055, 1.6430522203445, 8.5526323318481, 7.6237897872925, 3.2832958698273, 1.7167723178864, 5.5215029716492 ]), ("83" -> [ -32.109405517578, 3.9070281982422, 1.0687181949615, 8.1547784805298, 2.9725654125214, 3.0180866718292, 4.1565504074097, 8.5721740722656, 7.3978796005249, 5.3308491706848, 4.4803323745728, 6.3259897232056 ]), ("16" -> [ -25.844465255737, -3.5769534111023, 1.5508754253387, 12.367392539978, 5.7629737854004, 0.85355216264725, 0.14492024481297, 8.8884382247925, 11.024919509888, 1.3375108242035, -2.8835127353668, 7.2457590103149 ]) ]), ("cols" -> 12) ];

		reducedarray_target = [ [ -2.7634406895916, -0.67866026889842 ], [ -0.30971704641996, -2.8118519169002 ], [ -0.96944190976746, 1.8509311477306 ], [ 0.59201734657228, 0.60220555613272 ], [ 6.2080632966176, 0.11662924744608 ], [ 1.7880437248266, -4.6878583265354 ], [ -0.86532970365306, 1.2601110215871 ], [ -0.67692646456523, 0.65313561533388 ], [ 0.88762076423901, -2.5030001513173 ], [ 3.1071725154777, 0.95254918554645 ], [ -0.056324333290545, -0.012528345455183 ], [ -1.4616378419552, 0.29840103573348 ], [ -1.3730320363504, -4.2286501709727 ], [ 1.2651608891623, -2.6148835459648 ], [ -0.61047392572511, 0.91264942753251 ], [ -0.97685999221274, -0.91262071102544 ], [ 1.3632332252558, 1.4705697508793 ], [ 5.7496668573066, 0.077024018815553 ], [ 5.6144458418002, 1.1103577852864 ], [ 3.7683251924263, 0.48856986657265 ], [ 0.99796659390383, -3.3975306049375 ], [ -1.8350505586056, 1.9352721488206 ], [ -2.4813170344746, 1.3030276226844 ], [ 0.70560025354219, -2.536178021415 ], [ -1.8375508704013, 2.1017605346306 ], [ -2.1906130703986, 0.38831020179726 ], [ 0.32574285862168, -1.2218811702249 ], [ -1.9112364703579, 1.6253654016161 ], [ -2.5092709986924, 0.88827962777429 ], [ -1.6708219232646, -3.4966902632321 ], [ 0.69468449747234, 1.323120971122 ], [ 4.1912341013686, -0.66375261692472 ], [ 4.9125751700856, -1.3141572250297 ], [ 1.9687470173336, 1.6027537795898 ], [ -1.8141187055704, 1.4485097404986 ], [ 2.6011292742752, -2.6421625451359 ], [ 5.7790363002436, 1.1749666598977 ], [ 3.6889339721785, -1.6734854222278 ], [ 0.11875057070163, 2.0875179410274 ], [ -1.9453135696683, 1.5965898055688 ], [ 3.8000198433547, 1.0584343030287 ], [ 1.4442001324087, 2.5377397927358 ], [ -1.4368149147308, 1.648197423903 ], [ 0.54824888044541, -0.13331884282949 ], [ 2.8505872627661, -0.90536237552675 ], [ -1.3242867290398, 1.6105621402146 ], [ -2.1698480573366, 1.0046857101051 ], [ -2.4104175047887, 0.37347335210654 ], [ 5.3040785201395, 1.7604993824866 ], [ -0.98844668569847, 1.3123478522049 ], [ -1.1229317915448, -4.8458207015595 ], [ -1.1705945922941, 1.3335013333226 ], [ -2.1520462469146, 1.2284150977128 ], [ -1.4040351300001, -0.0083226816538363 ], [ 5.6720763118763, 1.0483270859228 ], [ 0.26847460450546, 2.5609664932301 ], [ -1.8483228604736, 1.1514578613418 ], [ -2.1883604199512, -1.7899741610069 ], [ -0.54829115022745, -1.1752148160179 ], [ -1.6806624373358, 1.2818999700025 ], [ -1.5253852488585, -2.0948934665891 ], [ 5.9633309204812, 0.67148117628365 ], [ 4.2705365129772, -0.27041466768915 ], [ -0.95746033305644, 0.15045899850097 ], [ -1.1526628948848, -1.0073884129722 ], [ -2.2072887893939, 1.3820087744552 ], [ -1.899927847783, 0.63316189148569 ], [ -2.3218516121415, 1.2027160226536 ], [ -2.4592292396707, 0.036579627561109 ], [ -1.3093991660342, -0.21808493950039 ], [ -1.6409514917853, 0.83395565777999 ], [ -1.384276619179, -2.8970530710015 ], [ -1.2850101010464, 1.3110376962071 ], [ -1.9764427613591, -1.3760467311061 ], [ -1.5398404522875, 0.28311309535193 ], [ -1.0112012517008, -2.453272085319 ], [ -2.1435726476906, 1.4841564166819 ], [ -1.4696944330469, 0.64850122097757 ], [ 4.866077781596, 0.4354943274721 ], [ 0.40381752885066, -0.68940122689244 ], [ -2.1894576993047, 1.7278361219089 ], [ -1.6307495429531, -3.9886598681407 ], [ -0.90900114743536, -0.92540332241467 ], [ -2.2996860671515, 1.2387474016314 ], [ -2.435283135098, 0.61818135769188 ], [ 5.9229073406196, 1.2968620691793 ], [ 4.9528284543438, -0.64458116366314 ], [ 0.83316991724901, 2.5329004844876 ], [ -1.7852335638957, 1.5296382112696 ], [ -1.860204623835, 1.1253606176626 ], [ -2.0600665200433, -2.3704169244599 ], [ -1.8678149254085, 1.9970868834842 ], [ -1.3199668266287, 0.22575430310014 ], [ -1.610578345128, 0.98170193302039 ], [ -2.3500832868299, -1.3821760587532 ], [ -0.60115082922619, 0.351600885922 ], [ -1.3919521135437, -5.336979406424 ], [ -0.93782078740059, 1.3830068336921 ], [ -1.0521670480275, -1.7597206921067 ], [ -2.1395572578955, 0.40793902142056 ] ];
	}

	test_mfcc {
		var condition = Condition();
		var raw = FluidDataSet(server);
		var standardized = FluidDataSet(server);
		var reduced = FluidDataSet(server);
		var datapoint = Buffer.alloc(server, 12);
		var standardizer  = FluidStandardize(server);
		var pca = FluidPCA(server,2);

		var reducedarray;

		var inbuf = Buffer.loadCollection(server,0.5.dup(12));
		var outbuf = Buffer.new(server);

		var point;

		raw.load(mfcc_dict);

		server.sync;

		reducedarray = Array.new(100);
		standardizer.fitTransform(raw, standardized);
		pca.fitTransform(standardized, reduced, action:{|x|
			result[\variance] = TestResultEquals(x.asFloat, 0.56128084659576, 0.0001);
			reduced.dump{|x|
				var data = x["data"];
				result[\pca_size] = TestResult(data.size, 100);
				100.do{|i|
					reducedarray = reducedarray.add(data[i.asString])
				};
				condition.unhang;
			};
		});

		condition.hang;

		result[\reducedarray] = TestResultEquals(reducedarray.abs, reducedarray_target.abs, 0.0001);

		pca.transformPoint(inbuf,outbuf,{|x| x.getn(0,1,{|y| point = y.asArray; condition.unhang})});

		condition.hang;

		result[\point] = TestResultEquals(point, [ -0.23524188995361 ].abs, 0.0001);

		server.sync;
		raw.free;
		standardized.free;
		reduced.free;
		standardizer.free;
		pca.free;
	}
}