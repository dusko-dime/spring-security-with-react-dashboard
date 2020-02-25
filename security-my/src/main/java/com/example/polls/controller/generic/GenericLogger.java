//package com.example.polls.controller.generic;
//
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.GenericTypeResolver;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import com.rits.cloning.Cloner;
//
//import javax.servlet.http.HttpSession;
//
///**
// * Created by drstjepanovic on 8/28/2017.
// */
//public class GenericLogger<T> {
//
//    public static Log log = LogFactory.getLog(GenericLogger.class.getName());
//    protected Cloner cloner;
//    private Class<T> type;
//    private HttpSession httpSession;
//    @Autowired
//    private LoggerRepository loggerRepository;
//    @Autowired
//    private UserBea userBean;
//    @Value("${loggerConfig.createMessage}")
//    private String createMessage;
//    @Value("${loggerConfig.updateMessage}")
//    private String updateMessage;
//    @Value("${loggerConfig.deleteMessage}")
//    private String deleteMessage;
//
//    public GenericLogger() {
//        cloner = new Cloner();
//        this.type = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), GenericLogger.class);
//        this.httpSession = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession(true);
//    }
//
//    public void logCreateAction(T object) {
//        loggerRepository.saveAndFlush(new Logger(userBean.getUser().getId(), Logger.ActionType.CREATE.toString(), createMessage.replace("{entity}", object.toString()), type.getSimpleName(), (byte) 1));
//    }
//
//    public void logUpdateAction(T newObject, T oldObject) {
//        loggerRepository.saveAndFlush(new Logger(userBean.getUser().getId(), Logger.ActionType.UPDATE.toString(), updateMessage.replace("{oldEntity}", oldObject.toString()).replace("{newEntity}", newObject.toString()), type.getSimpleName(), (byte) 1));
//    }
//
//    public void logDeleteAction(T object) {
//        loggerRepository.saveAndFlush(new Logger(userBean.getUser().getId(), Logger.ActionType.DELETE.toString(), deleteMessage.replace("{entity}", object.toString()), type.getSimpleName(), (byte) 1));
//    }
//
//    public void logSpecificAction(String actionType, String actionDetails, String tableName) {
//        loggerRepository.saveAndFlush(new Logger(userBean.getUser().getId(), actionType, actionDetails, tableName, (byte) 0));
//    }
//
//    public HttpSession getHttpSession() {
//        return httpSession;
//    }
//
//    protected void logError(Exception e, Log log){
//        log.error(e);
//        for (StackTraceElement element : e.getStackTrace())
//            log.error(element.toString());
//    }
//
//
//
//}