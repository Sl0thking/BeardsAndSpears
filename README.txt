##################################################################################################
##					BEARDS & SPEARS						##
##################################################################################################


##### 0. Inhaltsverzeichnis ######################################################################
	1. Das Spiel
	2. Architektur des Spiels und Neuronalen Netzes
	3. Funktionsübersicht
	4. Konfigurieren und Testen



##### 1. Das Spiel ###############################################################################
Verteidige dich möglichst ruhmreich gegen den gegnerischen Ansturm von Wikingern.
Ruhm erhält man über verschiedene Aktionen:
-Überleben (Punkte über Zeit)						(001 Ruhm pro x Frames)
-Sammeln von Speeren, die gegnerische Wikinger auf dich geworfen haben. (050 Ruhm)
-Besiegen von feindlichen Wikingern.					(100 Ruhm)

Versuche also möglichst lange zu überleben und dabei möglichst viele Gegner zu besiegen!

Solltest du dies nicht dem neuronalen Netz überlassen sondern selber spielen, beachte die 
folgende Steuerung:
A - Bewegung nach Links
D - Bewegung nach Rechts
F - Werfen eines Speers (wenn Speere aufgesammelt wurden).



##### 2. Architektur des Spiels & Neuronales Netz ##################################################
Das Spiel und das neuronale Netz wurde auf Basis einer ECS (Entity, Component, System) Engine namens 
SlothEngine (Eigententwicklung) implementiert. Dies bedeutet, dass Spielfunktionen oder Spielobjekte 
durch Systeme oder Entitäten repräsentiert werden. Entitäten werden nicht durch eine Erbreihenfolge 
spezialisiert, sondern über an die Entität gekoppelte Komponenten (Beispielsweise müssen Entitäten,
die gerendert werden sollen, eine Positions- und Spritekomponente haben). Daher werden sämtliche 
spielrelevanten Daten grundsätzlich über Komponente gespeichert oder abgerufen (Aktuell gibt es noch
Ausnahmen, die zukünftig noch in Komponenten ausgelagert werden müssen).

Sämtliche Entitäten werden in einer Entitäten Liste gespeichert, die vom EntitätenManager verwaltet 
wird. Die Systeme haben eine Referenz auf diesen Manager, um mit den vorhandenen Entitäten arbeiten 
zu können. Getriggert werden Systeme entweder jeden Frame oder durch Nachrichten. Das Verhalten von 
Systemen wird durch sogennante Verhaltensweisen definiert. Diese werden implementiert und bei dem 
gewünschten System registriert. So gibt es beispielsweise ein System was die Gegner kontrolliert 
und zufällig erscheinen lässt, was Speere die geworfen wurden in die entsprechende Richtung 
weiterfliegen lässt oder Kollisionen zwischen den Objekten regelt.

Entsprechend dieser Architektur wurde auch das neuronale Netz für dieses Spiel umgesetzt. 
Lernen tut es über einen genetischen Algorithmus. Das neuronale Netz selber ist ein 
selbstgeschriebener Graph, welcher sich in einer auf neuronale Netze spezialisierten Komponente 
befindet. Zur Umsetzung des genetischen Algorithmus wurde ein genetisches System mit ensprechenden 
Verhalten implementiert. Dieses System hat Zugriff auf die Entität mit der neuronalen Netz 
Komponente. Mit dem darin enthaltenen Graphen und anderen notwendigen Werten ändert es den jeweiligen 
Aufbau des aktiven Graphen, bewertet dessen Einstellung anhand des gesammelten Ruhms, züchtet neue 
Einstellungen und mutiert diese anschließend.

Die Kanten in dem Neuronalen Netz haben einen Wert zwischen Null und Eins. Um diese 
Einstellung/Werte zu speichern und es sehr einfach mutierbar zu machen, entschiedenen wir uns für eine
binäre Darstellung, nachfolgend genannt Netzwerksequenz. Das Netz kann diese verarbeiten und übernehmen. 
Diese bestehen aus einer langen Zeichenkette von Nullen und Einsen. Der Graph kann diese Zeichenfolge 
auslesen und verarbeiten. Jeder Kantenwert ist durch Acht-Bit (Acht Zeichen) kodiert:
MSB : Vorzeichen
Stelle 2^7 - 2^1 : Zahl zwischen 0 und 127
Der Graph liest jeweils Acht Zeichen der Kette aus, wandelt es in die entsprechende Dezimalpräsentation 
um und teilt die daraus resultierende Zahl durch 127, um so eine Zahl zwischen Null und Eins der 
jeweiligen Kante zu zuweisen.



##### 3. Funktionsübersicht ###########################################################################
-Manuelles Spielen
-selbstgeschriebenes Neuronales Netz, welches über einen genetischen Algorithmus das Spiel lernt (mit/ohne GUI)
-Erstellung / Verwaltung von Lernarchiven
-Speichern und Laden von Netzwerksequenzen / Generationen



##### 4. Konfigurieren & Testen #######################################################################
Sämtliche Spieleinstellungen werden über Zwei Konfigurationsdateien vorgenommen:
valHal.properties
learn.properties

Die erstere Konfigurationsdatei ist für grundlegende Einstellungen oder Konfigurationen für das Spielen 
ohne Netz ausgelegt. Wichtig sind hier vor allem folgende Einstellungen:
archivePath=.		//Gibt den Pfad zum Lernarchiv an
isKi=false			//Spielt das Netz oder man selber?
gameSpeed=1			//Erhöht die Spielgeschwindigkeit ums xfache. Wichtig zum lernen.
learnArchiveID=666	//Gibt die IDs des Archivs an.
showGui=true		//Zeigt die GUI an oder nicht.

Dabei ist das Konzept des Lernarchivs wichtig. Um unterschiedliche Populationen oder Einstellungen speichern zu
können, werden Lernarchive erstellt. Diese haben eine eigene Konfigurationsdatei sowie Ordner, um
Generationen zu speichern. Welches Archiv benutzt/erstellt werden soll wird durch archivePath und der 
learnArchiveID angegeben. Das Spiel speichert/überschreibt automatisch jede neue Generation im Archiv, daher 
kann der Lernprozess jederzeit abgebrochen werden und bei der letzten !!vollständigen!! Generation fortgesetzt 
werden. Die Archiv spezifische Konfigurationsdatei hat teilweise die selben Einstellungen wie die valHal.properties, 
aber weitere die vor allem das Lernen des Netzes betreffen:
mutateChance=0.05	//Chance, dass ein einzelnes Bit der Netzwerksequenz "kippt" beim Mutationsprozess
nnGenerations=500	//Anzahl der Generationen, die beim lernen durchlaufen werden sollen
isQuiet=true		//Beschränkt die Konsolenausgabe auf ein Minimum 
nnMaxPop=16			//Maximale Population pro Generation
isLearning=true		//Gibt an ob gelernt werden soll oder eine Sequenz im replay mode wiedergeben werden soll
nnSizeOfElite=14	//Gibt an, wieviele Generation nach der Bewertung überleben sollen 
					//(in diesem Beispiel würden 14 überleben und 2 sterben, da die max Population auf 16 gesetzt ist.)

Wenn Einstellungen doppelt vorkommen, haben während des Lernen die Einstellungen des Lernarchivs den Vorzug. 
Einzelne Sequenzen können in den Replay Ordner verschoben werden und betrachtet werden. Lediglich die Option 
isLearning muss auf false gesetzt werden.

Als Beispielarchive sind das Lernarchiv 666 und 777 vorhanden. Das Archiv 666 hat schon länger gelernt und kann 
das Spiel schon etwas besser, während das Archiv 777 völlig untrainiert ist.
Die für das Archiv 666 vorgenommene Einstellungen haben sich als gute Einstellungen bewährt. Auch die 
Einstellungen der valHal.properties sind für die Abgabe auf einen guten Lernfortschritt eingestellt. Generell 
sollten für einen schnellen Lernprozess die Spielgeschwindigkeit erhöht, die GUI ausgestellt und die Leben des Spielers 
auf Eins gesetzt werden.