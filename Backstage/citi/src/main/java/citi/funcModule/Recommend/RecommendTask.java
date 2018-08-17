package citi.funcModule.Recommend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static citi.funcModule.Recommend.RecommendService.cosineSimilarity;

@Component
public class RecommendTask {
    @Autowired
    private RecommendService recommendService;
    //@Scheduled(cron="0/30 * * * * ? ") //间隔5秒执行
    public void test(){
        System.out.println("定时任务开始啦，哈哈哈");
        System.out.println("<<<---------结束执行HR数据同步任务--------->>>");
    }

    /*
     * 更新商户相似度
     */
    //@Scheduled(cron="0 0 24 * * ?")
    public ArrayList<MerchantSimilarity> updateMerchantSimilarities(){
        ArrayList<MerchantSimilarity> results = new ArrayList<MerchantSimilarity>();
        ArrayList<RecommendService.MerchantPoints> merchantPoints = recommendService.getMerchantPointsArray();
        for(int i=0;i<merchantPoints.size()-1;i++){
            for(int j=i;j<merchantPoints.size();j++){
                double similarity = cosineSimilarity(merchantPoints.get(i).points,merchantPoints.get(i).points);
                String merchantID1 = merchantPoints.get(i).merchantID;
                String merchantID2 = merchantPoints.get(j).merchantID;
                results.add(new MerchantSimilarity(merchantID1,merchantID2, similarity));
            }
        }
        try {
            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("MerchantSimilarity.txt"));
            for(MerchantSimilarity result:results){
                oos.writeObject(result);
            }
            oos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return results;
    }

    //@Scheduled(cron="0 0 23 * * ?")
    public ArrayList<ItemSimilarity> updateItemSimilarities(){
        ArrayList<ItemSimilarity> results = new ArrayList<ItemSimilarity>();
        ArrayList<RecommendService.ItemPoints> itemPoints = recommendService.getItemPointsArray();
        for(int i=0;i<itemPoints.size()-1;i++){
            for(int j=i;j<itemPoints.size();j++){
                double similarity = cosineSimilarity(itemPoints.get(i).points,itemPoints.get(j).points);
                String itemID1 = itemPoints.get(i).itemID;
                String itemID2 = itemPoints.get(j).itemID;
                results.add(new ItemSimilarity(itemID1,itemID2, similarity));
            }
        }
        try {
            ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("ItemSimilarity.txt"));
            for(ItemSimilarity result:results){
                oos.writeObject(result);
            }
            oos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return results;
    }
}
