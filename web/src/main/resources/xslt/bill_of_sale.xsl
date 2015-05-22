<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : newstylesheet.xsl
    Created on : 28. dubna 2015, 10:03
    Author     : Calime
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/contract">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>Smlouva o koupi motorového vozidla</title>
                <link rel="stylesheet" type="text/css" href="css.css" />
            </head>
            <body>
                <h1>Kupní smlouva o prodeji motorového vozidla</h1>
                <h2>Čl. I.
                    <br />Smluvní strany</h2>
                <p>Firma:
                    <br />Sídlem: Botanická 68A
                    <br />IČ: 22052015
                    <br />dále jen „prodávající“ na straně jedné
                </p>
                <br />
                <p>a</p>
                <br />
                <p>Jméno a příjmení: <xsl:value-of select="customer/name"></xsl:value-of>
                    <br />Bytem: <xsl:value-of select="customer/adress"></xsl:value-of>
                    <br />Datem narození: <xsl:value-of select="customer/dateOfBirth"></xsl:value-of>
                    <br />dále jen „kupující“ na straně druhé</p>
                <br />
                <p>uzavírají níže uvedeného dne, měsíce a roku v souladu s ustanovením § 2079 a násl. zákona č. 89/2012 Sb., občanský zákoník, smlouvu o prodeji motorového vozidla.</p>
                <h2>Čl. II.
                    <br />Prohlášení prodávajícího</h2>
                <p>Prodávající tímto prohlašuje, že je výlučným vlastníkem motorového vozidla uvedeného v čl. III této smlouvy a že na předmětu koupě neváznou práva třetích osob ani jiná omezení.</p>
                <h2>Čl. III.
                    <br />Předmět smlouvy</h2>
                <p>Předmětem této smlouvy je prodej a koupě níže uvedeného motorového vozidla:</p>
                    <p>Tovární značka: <xsl:value-of select="car/manufacturer"></xsl:value-of></p>
                    <p>Barva vozidla: 
                        <xsl:value-of select="car/color"></xsl:value-of>
                    </p>
                    <p>Počet ujetých kilometrů: 
                        <xsl:value-of select="car/km"></xsl:value-of>
                    </p>
                    <p>Počet klíčů: 2</p>
                    <p>Stav vozidla při prodeji:
                        <xsl:choose>
                            <xsl:when test="car/km>120000">menší technické závady</xsl:when>
                            <xsl:otherwise>zcela v pořádku</xsl:otherwise>
                        </xsl:choose>
                    </p>                
                    <p>Příslušenství motorového vozidla: náhradní kola </p>
                    
                    <h2>Čl. IV.
                        <br />Kupní cena</h2>
                    <p>Prodávající touto smlouvou a za podmínek v ní dohodnutých prodává kupujícímu výše uvedený osobní automobil společně s jeho příslušenstvím a kupující tento automobil za dohodnutou kupní cenu kupuje do svého výlučného vlastnictví, a to za dohodnutou kupní cenu ve výši <xsl:value-of select="price"></xsl:value-of> Kč</p>
                    <p>Kupní cenu kupující zaplatil převodem na bankovní účet prodávajícího č.: ………………/……… do 3 dnů od podpisu této smlouvy.</p>
                    <p>K zaplacení kupní ceny podle této smlouvy dochází dnem, kdy je příslušná částka připsána na účet prodávajícího.</p>
                    <h2>Čl. V.
                        <br />Prohlášení kupujícího</h2>
                    <p>Prodávající prohlašuje, že mu nejsou známy žádné skryté vady prodávaného automobilu, na které by kupujícího neupozornil.</p>
                    <p>Kupující prohlašuje, že se řádně seznámil se stavem prodávaného motorového vozidla, zejména se všemi případnými závadami popsanými v čl. III této smlouvy a v tomto stavu jej nabývá do svého vlastnictví.</p>
                    <h2>Čl. VI.
                        <br />Závěrečná ustanovení</h2>
                    <p>Nebezpečí škody na věci přechází na kupujícího převzetím věci.</p>
                    <p>Prodávající se zavazuje provést odhlášení předmětného vozidla na kupujícího z evidence motorových vozidel, a to nejpozději do 5 pracovních dnů od podpisu této smlouvy a předat kupujícímu doklady od vozidla (technický průkaz, osvědčení o registraci vozidla), které z důvodu provedení změn v evidenci vozidel nemohl předat při podpisu smlouvy.</p>
                    <p>Prodávající si sjednává výhradu vlastnického práva k předmětu koupě. Smluvní strany se dohodly, že předmět koupě zůstává až do úplného zaplacení celé kupní ceny uvedené v čl. II ve vlastnictví prodávajícího. Kupující se musí zdržet jakékoliv dispozice s vozidlem, která by mohla ohrozit výhradu vlastnictví prodávajícího.</p>
                    <p>Zaplacením kupní ceny uvedené v čl. II této smlouvy, přechází na kupujícího vlastnické právo prodávaného motorového vozidla.</p>
                    <p>
                        <xsl:if test="car/km>150000"> Z důvodu vysokého stáří vozidla a nejasného počtu skutečně najetých kilometrů se prodávající odvolává na ustanovení podle § 1916 NOZ:
                    Podpisem této smlouvy kupující výslovně prohlašuje, že se vzdává svých práv z vad zboží.</xsl:if>
                    </p>
                <p>Smluvní strany prohlašují, že jsou plně svéprávné k právnímu jednání, že si smlouvu před podpisem přečetly, s jejím obsahem souhlasí a na důkaz toho připojují své podpisy.</p>
                <p>Tato smlouva nabývá platnosti a účinnosti dnem jejího podpisu oběma smluvními stranami.</p>
                <p>Tato smlouva se uzavírá ve dvou vyhotoveních, z nichž každá smluvní strana obdrží jedno.</p>
                <br />
                <table>
                    <tr>
                        <td>
                            <p>V ……............. dne     </p>
                        </td>   
                        <td>
                            <p>V….................. dne     </p>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <p>-----------------------------</p>
                        </td>
                        <td> 
                            <p>-----------------------------</p>
                        </td>
                    </tr>                      
                 <tr>
                        <td>
                            <p>Prodávající</p> 
                        </td> 
                        <td>
                            <p>Kupující</p>
                        </td>
                 </tr>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
