//package com.example.twogether.alarm.repository;
//
//import java.util.Map;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//public interface EmitterRepository {
//    SseEmitter saveEmitter(String emitterId, SseEmitter sseEmitter);
//    void saveEvent(String emitterId, Object event);
//
//    Map<String, SseEmitter> findAllEmitterStartWithByUserId(String userId);
//    Map<String,Object> findAllEventCacheStartWithByUserId(String userId);
//
//    void deleteById(String emitterId);
//    void deleteAllEmitterStartWithId(String userId);
//    void deleteAllEventCacheStartWithId(String memberId);
//}
