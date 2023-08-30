package com.example.twogether.alarm.service;

import com.example.twogether.alarm.dto.AlarmsResponseDto;
import com.example.twogether.alarm.entity.Alarm;
import com.example.twogether.alarm.repository.AlarmRepository;
import com.example.twogether.common.error.CustomErrorCode;
import com.example.twogether.common.exception.CustomException;
import com.example.twogether.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    @Transactional
    public void createAlarm(Alarm alarm) {
        alarmRepository.save(alarm);
    }

    @Transactional(readOnly = true)
    public AlarmsResponseDto getAlarms(User user) {

        //if(boardColRepository.existsByUser_IdOrBoardCollaborator_IdAndBoard_Id(user.getId(), boardId)) {
        List<Alarm> alarms = alarmRepository.findAllByReceiver(user);
        return AlarmsResponseDto.of(alarms);
    }

    @Transactional
    public void readAlarm(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(() ->
            new CustomException(CustomErrorCode.ALARM_NOT_FOUND));

        alarm.read();
        alarmRepository.save(alarm);
    }

    @Transactional
    public void deleteAlarm(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(() ->
            new CustomException(CustomErrorCode.ALARM_NOT_FOUND));

        alarmRepository.delete(alarm);
    }

//    private final EmitterRepository emitterRepository;
//
//    private static final Long SSE_END_TIME = (60 * 60 * 1000) * 3L;
//
//    // 로그인 후 알림 구독을 위해 필요한 emitter 생성
//    public SseEmitter subscribe(Long userId, String lastEventId) {
//        String emitterId = createIdByUserId(userId);
//        SseEmitter emitter = emitterRepository.saveEmitter(emitterId, new SseEmitter(SSE_END_TIME));
//
//        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
//        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
//        emitter.onError((e) -> emitterRepository.deleteById(emitterId));
//        emitterRepository.saveEmitter(emitterId, emitter);
//
//        // 503 에러를 방지하기 위한 더미 이벤트 전송
//        String eventId = createIdByUserId(userId);
//        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + userId + "]");
//
//        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
//        if (hasLostData(lastEventId)) {
//            sendLostData(lastEventId, userId, emitterId, emitter);
//        }
//
//        return emitter;
//    }
//
//    public void sendAlarm(User receiver, AlarmTrigger alarmTrigger, String url) {
//        Alarm alarm = alarmRepository.save(new Alarm(receiver, alarmTrigger, url));
//        String receiverId = String.valueOf(receiver.getId());
//
//        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByUserId(receiverId);
//        sseEmitters.forEach(
//            (key, emitter) -> {
//                emitterRepository.saveEvent(key, alarm);
//                sendToClient(emitter, key, AlarmResponseDto.of(alarm));
//            }
//        );
//    }
//
//    private void sendToClient(SseEmitter emitter, String emitterId, Object data) {
//        try {
//            emitter.send(SseEmitter.event()
//                .id(emitterId)
//                .data(data));
//        } catch (IOException exception) {
//            emitterRepository.deleteById(emitterId);
//            throw new CustomException(CustomErrorCode.UNINVITED_BOARD);
//        }
//    }
//
//    private String createIdByUserId(Long userId) {
//        return userId + "_" + System.currentTimeMillis();
//    }
//
//    private boolean hasLostData(String lastEventId) {
//        return !lastEventId.isEmpty();
//    }
}
