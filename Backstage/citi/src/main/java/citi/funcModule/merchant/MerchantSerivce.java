package citi.funcModule.merchant;

import citi.persist.mapper.MerchantMapper;
import citi.vo.Merchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 接口设计：刘钟博
 * 代码填充：彭璇
 */
@Service
public class MerchantSerivce {

    @Autowired
    private MerchantMapper merchantMapper;

    /**
     * 获取商家列表
     * @param start 开始节点
     * @param length 长度
     * @return 商家列表
     */
    public List<Merchant> getMerchants(int start,int length){
        List<Merchant> merchants = merchantMapper.select(start,length);
        return merchants;
    }

    /**
     * 通过id获取商家
     * @param merchantID 商家ID
     * @return 商家
     */
    public Merchant getMerchant(String merchantID){
        return merchantMapper.selectByID(merchantID);
    }

    public int getNum(){
        return merchantMapper.getMerchantAmount();
    }

    public ArrayList<Merchant> search(String[] keywords){
        List<Merchant> merchants = merchantMapper.getAllMerchant();
        ArrayList<Merchant> results = new ArrayList<Merchant>();
        for(Merchant merchant:merchants){
            for(String keyword:keywords){
                if(merchant.getName().contains(keyword)){
                    results.add(merchant);
                    break;
                }
            }
        }
        if(results.size()==0){
            for(Merchant merchant:merchants){
                for(String keyword:keywords){
                    if(merchant.getDescription().contains(keyword)){
                        results.add(merchant);
                        break;
                    }
                }
            }
        }
        return results;
    }

    public int searchCount(String[] keywords){
        List<Merchant> merchants = merchantMapper.getAllMerchant();
        int counter=0;
        for(Merchant merchant:merchants){
            for(String keyword:keywords){
                if(merchant.getName().contains(keyword)){
                    counter++;
                    break;
                }
            }

        }
        if(counter==0){
            for(Merchant merchant:merchants){
                for(String keyword:keywords){
                    if(merchant.getDescription().contains(keyword)){
                        counter++;
                        break;
                    }
                }

            }
        }
        return counter;
    }
}
