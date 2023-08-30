//package com.example.twogether.alarm.repository;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.stream.Collectors;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//public class EmitterRepositoryImpl implements EmitterRepository {
//
//    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
//    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();
//
//    @Override
//    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
//        emitters.put(emitterId,sseEmitter);
//        return sseEmitter;
//    }
//
//    @Override
//    public void saveEvent(String emitterId, Object event) {
//        eventCache.put(emitterId,event);
//    }
//
//    @Override
//    public Map<String, SseEmitter> findAllEmitterStartWithByUserId(String userId) {
//        return emitters.entrySet().stream()
//            .filter(entry -> entry.getKey().startsWith(userId))
//            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }
//
//    @Override
//    public Map<String, Object> findAllEventCacheStartWithByUserId(String userId) {
//        return eventCache.entrySet().stream()
//            .filter(entry -> entry.getKey().startsWith(userId))
//            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//    }
//
//    @Override
//    public void deleteById(String emitterId) {
//        emitters.remove(emitterId);
//
//    }
//
//    @Override
//    public void deleteAllEmitterStartWithId(String userId) {
//        emitters.forEach(
//            (key,emitter) -> {
//                if(key.startsWith(userId)){
//                    emitters.remove(key);
//                }
//            }
//        );
//    }
//
//    @Override
//    public void deleteAllEventCacheStartWithId(String memberId) {
//        eventCache.forEach(
//            (key,emitter) -> {
//                if(key.startsWith(memberId)){
//                    eventCache.remove(key);
//                }
//            }
//        );
//
//    }
//}
