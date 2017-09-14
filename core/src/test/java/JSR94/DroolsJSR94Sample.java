package JSR94;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;

import JSR94.bean.PointDomain;
import com.alibaba.fastjson.JSON;
import org.drools.jsr94.rules.RuleServiceProviderImpl;

/**
 * @version : 1.0
 * @Project : ms-pharos
 * @Program Name : DroolsJSR94Sample
 * @Class Name : DroolsJSR94Sample
 * @Copyright : Copyright (c)2017-2015
 * @Company : CreditEase
 * @Description :
 * @Author : tongwei
 * @Creation Date : 2017/9/14 15:43
 * @ModificationHistory Date             Author            Version           Description
 * ------------------------------------------------------------------
 * 2017/9/14         tongwei             1.0             1.0 Version
 */
public class DroolsJSR94Sample {

    private RuleServiceProvider ruleProvider;
    private String uri = RuleServiceProviderImpl.RULE_SERVICE_PROVIDER;
    public boolean init() {

        try {
            //1.注册ruleProvider,并且从RuleServiceProviderManager获取ruleProvider
            RuleServiceProviderManager.registerRuleServiceProvider(uri, RuleServiceProviderImpl.class);
            ruleProvider = RuleServiceProviderManager.getRuleServiceProvider(uri);

            HashMap<String, String> properties = new HashMap<String, String>();

            //2.获取RuleAdministrator实例,获取RuleExectuionSetProvider
            RuleAdministrator admin = ruleProvider.getRuleAdministrator();
            LocalRuleExecutionSetProvider ruleExecutionSetProvider = admin.getLocalRuleExecutionSetProvider(properties);

            //3.创建RuleExecutionSet
            Reader reader = new InputStreamReader(
                    new FileInputStream("F:\\code\\hydra\\core\\src\\test\\java\\JSR94\\addpoint.drl"));
            RuleExecutionSet reSet = ruleExecutionSetProvider.createRuleExecutionSet(reader, properties);

            //4.注册RuleExecutionSet
            admin.registerRuleExecutionSet("mysample", reSet, properties);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void run() {
        try {

            //5.获取RuleRuntime, 创建会话
            RuleRuntime runtime = ruleProvider.getRuleRuntime();
            StatelessRuleSession ruleSession = (StatelessRuleSession) runtime
                    .createRuleSession("mysample", null, RuleRuntime.STATELESS_SESSION_TYPE);

            //6.运行实例
            PointDomain pointDomain = new PointDomain();
            fillPointDomain(pointDomain);
            System.out.println(JSON.toJSONString(Arrays.asList(pointDomain)));
            ruleSession.executeRules(Arrays.asList(pointDomain));
            //            @SuppressWarnings("unchecked")
            //          List<PointDomain> results = (List<PointDomain>)ruleSession.executeRules(Arrays.asList(pointDomain));

            System.out.println("执行完毕BillThisMonth：" + pointDomain.getBillThisMonth());
            System.out.println("执行完毕BuyMoney：" + pointDomain.getBuyMoney());
            System.out.println("执行完毕BuyNums：" + pointDomain.getBuyNums());

            System.out.println("执行完毕规则引擎决定发送积分：" + pointDomain.getPoint());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillPointDomain(PointDomain pointDomain) {
        pointDomain.setUserName("hello kity");
        pointDomain.setBackMondy(100d);
        pointDomain.setBuyMoney(500d);
        pointDomain.setBackNums(1);
        pointDomain.setBuyNums(5);
        pointDomain.setBillThisMonth(5);
        pointDomain.setBirthDay(true);
        pointDomain.setPoint(0l);
    }

    public static void main(String... strings){
        DroolsJSR94Sample droolsJSR94Sample =  new DroolsJSR94Sample();
        droolsJSR94Sample.init();
        droolsJSR94Sample.run();
    }
}
