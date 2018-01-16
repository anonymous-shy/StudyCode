package xyz.shy;

import java.io.IOException;
import java.util.List;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.StopLibrary;
import org.ansj.recognition.impl.NatureRecognition;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * Created by Shy on 2018/1/12
 */

public class NatureDemo {
    public static void main(String[] args) throws IOException {
//        List<Term> terms = ToAnalysis.parse("Ansj中文分词是一个真正的ict的实现.并且加入了自己的一些数据结构和算法的分词.实现了高效率和高准确率的完美结合!");
//        Result parse = ToAnalysis.parse("Ansj中文分词是一个真正的ict的实现.并且加入了自己的一些数据结构和算法的分词.实现了高效率和高准确率的完美结合!");
//        new NatureRecognition(parse).recognition(); //词性标注
//        System.out.println(parse);
        StopRecognition stopRecognition = StopLibrary.get();
//        StopRecognition stopRecognition = new StopRecognition();
//        stopRecognition.insertStopWords("之所以");
        System.out.println(ToAnalysis.parse("我之所以觉得Ansj中文分词是一个不错的系统!我是also!"));
        System.out.println(ToAnalysis.parse("我之所以觉得Ansj中文分词是一个不错的系统!我是also!").recognition(stopRecognition));
    }
}
