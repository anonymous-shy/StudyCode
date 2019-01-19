//package xyz.shy.Consumer;
//
//import kafka.consumer.ConsumerIterator;
//import kafka.consumer.KafkaStream;
//import kafka.message.MessageAndMetadata;
//
///**
// * Created by Shy on 2017/11/28
// */
//
//public class ConsumerRun implements Runnable {
//    private KafkaStream m_stream;
//    private int m_threadNumber;
//
//    public ConsumerRun(KafkaStream a_stream, int a_threadNumber) {
//        m_threadNumber = a_threadNumber;
//        m_stream = a_stream;
//    }
//
//    public void run() {
//        ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
//
//        while (it.hasNext()) {
//            MessageAndMetadata<byte[], byte[]> metadata = it.next();
//            System.out.println("Thread [" + m_threadNumber + "] -> [Topic]: " + metadata.topic()
//                    + ", [Key]: " + new String(metadata.key())
//                    + ", [Partition]: " + metadata.partition()
//                    + ", [Offset]: " + metadata.offset()
//                    + ", [Message]: " + new String(metadata.message()));
//        }
//        System.out.println("Shutting down Thread: " + m_threadNumber);
//    }
//}
