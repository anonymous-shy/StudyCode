package xyz.shy;

import org.ansj.splitWord.analysis.*;

/**
 * Created by Shy on 2017/12/15
 * https://github.com/NLPchina/ansj_seg
 * https://github.com/NLPchina/ansj_seg/wiki
 * https://nlpchina.github.io/ansj_seg/
 * Spark下四种中文分词工具使用 http://blog.csdn.net/bianenze/article/details/76269720
 */

public class AnsjDemo1 {
    public static void main(String[] args) {

        String str = "洁面仪配合洁面深层清洁毛孔";
        System.out.println("BaseAnalysis:  " + BaseAnalysis.parse(str));
        System.out.println("ToAnalysis:    " + ToAnalysis.parse(str));
        System.out.println("DicAnalysis:   " + DicAnalysis.parse(str));
        System.out.println("IndexAnalysis: " + IndexAnalysis.parse(str));
        System.out.println("NlpAnalysis:   " + NlpAnalysis.parse(str));
    }
}
