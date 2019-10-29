package com.lzy.task;
import com.alibaba.dubbo.config.annotation.Reference;
import com.lzy.Service.OrderSettingService;
import com.lzy.utils.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CleanOrderSettingTask {

    @Reference
    private OrderSettingService orderSettingService;

    @Scheduled(cron = "0 0 2 L * ?")
    public void cleanOrderSetting1() {
        try {
            String beforeMonth = DateUtils.getBeforeMonth();
            orderSettingService.cleanOrderSetting(beforeMonth + "-32");
            System.out.println("预约设置删除成功");
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("预约设置删除失败");
        }
    }

   /*@Scheduled(cron = "0/10 * * * * ?")
    public void cleanOrderSetting2(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = sdf.format(new Date());
        System.out.println("任务2:" + s);
    }*/
}
