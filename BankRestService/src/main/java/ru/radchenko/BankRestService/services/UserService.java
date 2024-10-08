package ru.radchenko.BankRestService.services;


import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.radchenko.BankRestService.events.UserCreatedEvent;
import ru.radchenko.BankRestService.models.User;
import ru.radchenko.BankRestService.repositories.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final ApplicationEventPublisher eventPublisher;

    public UserService(UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public User getUserById(Long id){
            return  userRepository.findById(id).orElseThrow(() -> new RuntimeException("Получатель не найден с ID: " + id));
    }

    @Transactional
    public User createUser(User user){

    User savedUser = userRepository.save(user);

    // Публикация события
    eventPublisher.publishEvent(new UserCreatedEvent(savedUser.getId()));

    return  savedUser;

    }

}
