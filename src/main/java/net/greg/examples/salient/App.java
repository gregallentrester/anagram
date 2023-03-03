package net.greg.examples.salient;

import java.time.*;
import java.util.*;
import java.util.function.*;


/**
 * Three approaches to determining whether
 * two strings are anagrams of each other.
 *
 * https://bit.ly/3qUGbup
 * equivalency
 * https://community.oracle.com/tech/developers/discussion/4174550/java-11-instant-now-gives-microsecond-precision-but-we-need-nanosecond
 *
 */
public class App {

  /**
   * Exposition/comparison | a direct approach
   */
  private static void isAnagram(String token1, String token2) {

    long START = System.nanoTime();

    final Map<Character,Integer> mapOne = new HashMap();
    final Map<Character,Integer> mapTwo = new HashMap();

    char [] tokensOne = token1.toCharArray();
    char [] tokensTwo = token2.toCharArray();


    for (int i = 0; i < tokensOne.length; i++) {

      Integer temp = mapOne.get(tokensOne[i]);

      if (temp != null) {
        mapOne.put(tokensOne[i], ++temp);
      }
      else {
        mapOne.put(tokensOne[i], 1);
      }
    }

    for (int i = 0; i < tokensTwo.length; i++) {

      Integer temp = mapTwo.get(tokensTwo[i]);

      if (temp != null) {
        mapTwo.put(tokensTwo[i], ++temp);
      }
      else {
        mapTwo.put(tokensTwo[i], 1);
      }
    }

    System.err.println(
      "\n\n" + YLW + "\nMETHOD" + GRN + "\nisAnagram()\n" + NC);

    if (mapOne.equals(mapTwo)) {
      System.err.println(GRN + "\nStrings are Anagrams ");
    }
    else {
      System.err.println(RED + "\nStrings are not Anagrams ");
    }

    System.err.println
      (YLW + "Elapsed " +
      (System.nanoTime() - START) + NC);
  }


  /**
   * One-half traversal cost FOR INITIAL POPULATION (as compared to direct
   * the direct isAnagram() method's approach) is faster, INITIALLY.
   *
   * BUT another (second) traversal of the "other/second" String is still
   * required - and it involves TWO conditionals.
   *
   * Additionally, when any comparison is made which results in an incongruity
   * - unintuitively signaled by the map's value for a key/Character - logic
   * necessitates a short-circuiting of the logic (resulting in a confusing
   * context for reportage).
   */

   private static void isAnagramEBayWay(String token1, String token2) {

    long START = System.nanoTime();

    boolean isErrorLatch = false;

    final Map<Character,Integer> mapOne = new HashMap();

    char [] tokensOne = token1.toCharArray();


    for (int i = 0; i < tokensOne.length; i++) {

      Integer temp = mapOne.get((char) tokensOne[i]);

      if (temp != null) {
        mapOne.put(tokensOne[i], ++temp);
      }
      else {
        mapOne.put(tokensOne[i], 1);
      }
    }

    for (int i = 0; i < token2.length(); ++i) {

      if (mapOne.get(token2.charAt(i)) != null &&
          mapOne.get(token2.charAt(i)) > 0) {
          mapOne.put(token2.charAt(i), mapOne.get(token2.charAt(i))-1);
      }

      if (mapOne.get(token2.charAt(i)) == null ||
          token1.length() != token2.length()) {

        isErrorLatch = true;
        break;
      }
    }

    System.err.println(
      "\n\n" + YLW + "\nMETHOD" +
      GRN + "\nisAnagramEBayWay() \n\n" + NC);

    if (isErrorLatch) {

      System.err.println(
        RED + "\nStrings are not Anagrams");
    }
    else {
      System.err.println(GRN + "Strings are Anagrams");
    }

    System.err.println(
      YLW + "Elapsed " +
      (System.nanoTime() - START) + NC);
  }


  /**
   * Exposition/comparison | a direct approach
   */
  private static void isAnagramSanSort(String token1, String token2) {

    long START = System.nanoTime();

    int cnt = 0;

    if (token1.length() != token2.length()) {

      System.err.println(
        "\n\n" + YLW + "METHOD" + GRN + "\nisAnagramSanSort() \n" + NC +
        RED + "\n\nStrings are not Anagrams" + NC);
    }
    else {

      char[] a = token1.toUpperCase().toCharArray();
      char[] b = token2.toUpperCase().toCharArray();

      for (char c : a) {

        cnt = 0;  // reset

        for (char d : b) {

          if (c == d) {

            cnt++;
            continue;
          }
        }

        if (cnt == 0) break;  // A char in a was not found in b
      }

      //Arrays.sort(a);
      //Arrays.sort(b);

      if (cnt > 0) {

        System.err.println(
          "\n\n" + YLW + "METHOD" + GRN + "\nisAnagramSanSort() \n" + NC +
          GRN + "\n\nStrings are Anagrams" + NC);
      }
      else {

        System.err.println(
          "\n\n" + RED + "METHOD \nisAnagramSanSort() \n" + NC +
          RED + "\n\nStrings are not Anagrams" + NC);
      }

      System.err.println(
        YLW + "Elapsed " +
        (System.nanoTime() - START) + NC);
    }
  }


  /*
   * MODEL Options
   *    W+, W- | M+, M- | E+, E-
   *
   * ALGO  Options
   *    san | ana | ebay
   */
  public static void main(String ... args) {
    new App().selectUseCase(args[0], args[1]);  // ALGO, MODEL
  }


  private void selectUseCase(final String ALGO, final String MODEL) {

    // Alpha Model
    if ("W+".equalsIgnoreCase(MODEL) || "W-".equalsIgnoreCase(MODEL)) {
      handleWordModel(ALGO.toLowerCase(), MODEL);
    }
    // Embedded Spaces Model, Congruent
    else if ("E+".equalsIgnoreCase(MODEL) || "E-".equalsIgnoreCase(MODEL)) {
      handleEmbeddedSpacesModel(ALGO.toLowerCase(), MODEL);
    }
    // Embedded Spaces Model, Congruent
    else if ("M+".equalsIgnoreCase(MODEL) || "M-".equalsIgnoreCase(MODEL)) {
      handleMinifiedModel(ALGO.toLowerCase(), MODEL);
    }
  }

  private void handleWordModel(final String ALGORITHM, final String MODEL) {

    // Word Model, Congruent
    if ("W+".equalsIgnoreCase(MODEL)) {

      // CLI arg can match a subset of "san"
      if (SAN_SORT_METHOD.contains(ALGORITHM)) {
        isAnagramSanSort("carbon", "corban");
      }
      // CLI arg can match a subset of "gram"
      else if (ANAGRAM_METHOD.contains(ALGORITHM)) {
        isAnagram("carbon", "corban");
      }
      // CLI arg can match a subset of "ebay"
      else if (EBAY_METHOD.contains(ALGORITHM)) {
        isAnagramEBayWay("carbon", "corban");
      }
    }

    // Word Model, Incongruent/Disimilar
    else if ("W-".equalsIgnoreCase(MODEL)) {

      // CLI arg can match a subset of "san"
      if (SAN_SORT_METHOD.contains(ALGORITHM)) {
        isAnagramSanSort("carbo", "corban");
      }
      // CLI arg can match a subset of "gram"
      else if (ANAGRAM_METHOD.contains(ALGORITHM)) {
        isAnagram("carbo", "corban");
      }
      // CLI arg can match a subset of "ebay"
      else if (EBAY_METHOD.contains(ALGORITHM)) {
        isAnagramEBayWay("carbo", "corban");
      }
    }
  }


  private void handleMinifiedModel(final String ALGORITHM, final String MODEL) {

    // Minified Model, Congruent
    if ("M+".equalsIgnoreCase(MODEL)) {

      // CLI arg can match a subset of "san"
      if (SAN_SORT_METHOD.contains(ALGORITHM)) {
        isAnagramSanSort(modelNoSignatureMinified, modelNoSignatureMinified);
      }
      // CLI arg can match a subset of "gram"
      else if (ANAGRAM_METHOD.contains(ALGORITHM)) {
        isAnagram(modelNoSignatureMinified, modelNoSignatureMinified);
      }
      // CLI arg can match a subset of "ebay"
      else if (EBAY_METHOD.contains(ALGORITHM)) {
        isAnagramEBayWay(modelNoSignatureMinified, modelNoSignatureMinified);
      }
    }

    // Minified Model, Incongruent/Disimilar
    else if ("M-".equalsIgnoreCase(MODEL)) {

      // CLI arg can match a subset of "san"
      if (SAN_SORT_METHOD.contains(ALGORITHM)) {
        isAnagramSanSort(modelNoSignatureMinified, modelSignatureMinified);
      }
      // CLI arg can match a subset of "gram"
      else if (ANAGRAM_METHOD.contains(ALGORITHM)) {
        isAnagram(modelNoSignatureMinified, modelSignatureMinified);
      }
      // CLI arg can match a subset of "ebay"
      else if (EBAY_METHOD.contains(ALGORITHM)) {
        isAnagramEBayWay(modelNoSignatureMinified, modelSignatureMinified);
      }
    }
  }


  private void handleEmbeddedSpacesModel(final String ALGORITHM, final String MODEL) {

    if ("E+".equalsIgnoreCase(MODEL)) {
      // CLI arg can match a subset of "san"
      if (SAN_SORT_METHOD.contains(ALGORITHM)) {
        isAnagramSanSort(modelNoSignature, modelNoSignature);
      }
      // CLI arg can match a subset of "gram"
      else if (ANAGRAM_METHOD.contains(ALGORITHM)) {
        isAnagram(modelNoSignature, modelNoSignature);
      }
      // CLI arg can match a subset of "ebay"
      else if (EBAY_METHOD.contains(ALGORITHM)) {
        isAnagramEBayWay(modelNoSignature, modelNoSignature);
      }
    }

    // Embedded Spaces Model, Incongruent/Disimilar /Disimilar
    else if ("E-".equalsIgnoreCase(MODEL)) {

      // CLI arg can match a subset of "san"
      if (SAN_SORT_METHOD.contains(ALGORITHM)) {
        isAnagramSanSort(modelNoSignature, modelSignature);
      }
      // CLI arg can match a subset of "gram"
      else if (ANAGRAM_METHOD.contains(ALGORITHM)) {
        isAnagram(modelNoSignature, modelSignature);
      }
      // CLI arg can match a subset of "ebay"
      else if (EBAY_METHOD.contains(ALGORITHM)) {
        isAnagramEBayWay(modelNoSignature, modelSignature);
      }
    }
  }


  // CLI shorthand for sanSort method
  private static final String SAN_SORT_METHOD = "san";

  // CLI shorthand for anagram method
  private static final String ANAGRAM_METHOD = "ana";

  // CLI shorthand
  private static final String EBAY_METHOD = "ebay";



  // forward declarations for MODELS
  private final static String modelSignature;
  private final static String modelNoSignature;

  private final static String modelSignatureMinified;
  private final static String modelNoSignatureMinified;


  // MODEL Definitions
  static {

    modelSignature =
    new StringBuilder("Four score and seven years ago our fathers brought forth").
    append("upon this continent, a new nation, conceived in Liberty, and dedicated").
    append("to the proposition that all men are created equal.").
    append("Now we are engaged in a great civil war, testing whether that nation,").
    append("or any nation so conceived and so dedicated, can long endure. We are").
    append("met on a great battle-field of that war. We have come to dedicate a").
    append("portion of that field, as a final resting place for those who here").
    append("gave their lives that that nation might live. It is altogether fitting").
    append("and proper that we should do this.").
    append("But, in a larger sense, we can not dedicate—we can not consecrate—we").
    append("can not hallow—this ground. The brave men, living and dead, who").
    append("struggled here, have consecrated it, far above our poor power to add").
    append("or detract. The world will little note, nor long remember what we say").
    append("here, but it can never forget what they did here. It is for us the").
    append("living, rather, to be dedicated here to the unfinished work which").
    append("they who fought here have thus far so nobly advanced. It is rather").
    append("for us to be here dedicated to the great task remaining before").
    append("us—that from these honored dead we take increased devotion to that").
    append("cause for which they gave the last full measure of devotion—that we").
    append("here highly resolve that these dead shall not have died in vain—that").
    append("this nation, under God, shall have a new birth of freedom—and that").
    append("government of the people, by the people, for the people, shall not").
    append("perish from the earth.").
    append("—Abraham Lincoln").toString();

    modelNoSignature =
    new StringBuilder("Four score and seven years ago our fathers brought forth ").
    append("upon this continent, a new nation, conceived in Liberty, and dedicated").
    append("to the proposition that all men are created equal.").
    append("Now we are engaged in a great civil war, testing whether that nation,").
    append("or any nation so conceived and so dedicated, can long endure. We are").
    append("met on a great battle-field of that war. We have come to dedicate a").
    append("portion of that field, as a final resting place for those who here").
    append("gave their lives that that nation might live. It is altogether fitting").
    append("and proper that we should do this.").
    append("But, in a larger sense, we can not dedicate—we can not consecrate—we").
    append("can not hallow—this ground. The brave men, living and dead, who").
    append("struggled here, have consecrated it, far above our poor power to add").
    append("or detract. The world will little note, nor long remember what we say").
    append("here, but it can never forget what they did here. It is for us the").
    append("living, rather, to be dedicated here to the unfinished work which").
    append("they who fought here have thus far so nobly advanced. It is rather").
    append("for us to be here dedicated to the great task remaining before").
    append("us—that from these honored dead we take increased devotion to that").
    append("cause for which they gave the last full measure of devotion—that we").
    append("here highly resolve that these dead shall not have died in vain—that").
    append("this nation, under God, shall have a new birth of freedom—and that").
    append("government of the people, by the people, for the people, shall not").
    append("perish from the earth.").toString();

    modelSignatureMinified =
    new StringBuilder("Fourscoreandsevenyearsagoourfathersbroughtforth").
    append("uponthiscontinent,anewnation,conceivedinLiberty,anddedicated").
    append("tothepropositionthatallmenarecreatedequal.").
    append("Nowweareengagedinagreatcivilwar,testingwhetherthatnation,").
    append("oranynationsoconceivedandsodedicated,canlongendure.Weare").
    append("metonagreatbattle-fieldofthatwar.Wehavecometodedicatea").
    append("portionofthatfield,asafinalrestingplaceforthosewhohere").
    append("gavetheirlivesthatthatnationmightlive.Itisaltogetherfitting").
    append("andproperthatweshoulddothis.").
    append("But,inalargersense,wecannotdedicate—wecannotconsecrate—we").
    append("cannothallow—thisground.Thebravemen,livinganddead,who").
    append("struggledhere,haveconsecratedit,faraboveourpoorpowertoadd").
    append("ordetract.Theworldwilllittlenote,norlongrememberwhatwesay").
    append("here,butitcanneverforgetwhattheydidhere.Itisforusthe").
    append("living,rather,tobededicatedheretotheunfinishedworkwhich").
    append("theywhofoughtherehavethusfarsonoblyadvanced.Itisrather").
    append("forustobeherededicatedtothegreattaskremainingbefore").
    append("us—thatfromthesehonoreddeadwetakeincreaseddevotiontothat").
    append("causeforwhichtheygavethelastfullmeasureofdevotion—thatwe").
    append("herehighlyresolvethatthesedeadshallnothavediedinvain—that").
    append("thisnation,underGod,shallhaveanewbirthoffreedom—andthat").
    append("governmentofthepeople,bythepeople,forthepeople,shallnot").
    append("perishfromtheearth.").
    append("—AbrahamLincoln").toString();


    modelNoSignatureMinified =
    new StringBuilder("Fourscoreandsevenyearsagoourfathersbroughtforth").
    append("uponthiscontinent,anewnation,conceivedinLiberty,anddedicated").
    append("tothepropositionthatallmenarecreatedequal.").
    append("Nowweareengagedinagreatcivilwar,testingwhetherthatnation,").
    append("oranynationsoconceivedandsodedicated,canlongendure.Weare").
    append("metonagreatbattle-fieldofthatwar.Wehavecometodedicatea").
    append("portionofthatfield,asafinalrestingplaceforthosewhohere").
    append("gavetheirlivesthatthatnationmightlive.Itisaltogetherfitting").
    append("andproperthatweshoulddothis.").
    append("But,inalargersense,wecannotdedicate—wecannotconsecrate—we").
    append("cannothallow—thisground.Thebravemen,livinganddead,who").
    append("struggledhere,haveconsecratedit,faraboveourpoorpowertoadd").
    append("ordetract.Theworldwilllittlenote,norlongrememberwhatwesay").
    append("here,butitcanneverforgetwhattheydidhere.Itisforusthe").
    append("living,rather,tobededicatedheretotheunfinishedworkwhich").
    append("theywhofoughtherehavethusfarsonoblyadvanced.Itisrather").
    append("forustobeherededicatedtothegreattaskremainingbefore").
    append("us—thatfromthesehonoreddeadwetakeincreaseddevotiontothat").
    append("causeforwhichtheygavethelastfullmeasureofdevotion—thatwe").
    append("herehighlyresolvethatthesedeadshallnothavediedinvain—that").
    append("thisnation,underGod,shallhaveanewbirthoffreedom—andthat").
    append("governmentofthepeople,bythepeople,forthepeople,shallnot").
    append("perishfromtheearth.").toString();
  }

  public static final String RED = "\033[1;91m";
  public static final String GRN = "\033[1;92m";
  public static final String YLW = "\033[1;93m";
  public static final String NC = "\u001B[0m";
}
