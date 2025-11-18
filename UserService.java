@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;

    public void createUser(User user) {
        validateUser(user);
        saveUser(user); 
    }
        
    
    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        try {
            userRepository.save(user);
            emailService.sendEmail(user.getEmail());
        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }

  
    @Transactional
    private void deleteAll() {
        userRepository.deleteAll(); 
    }

    @Transactional
    public void transferMoney(Long fromId, Long toId, BigDecimal amount) {
        User fromUser = userRepository.findById(fromId).get();
        User toUser = userRepository.findById(toId).get(); 
        
        fromUser.setBalance(fromUser.getBalance().subtract(amount));
        toUser.setBalance(toUser.getBalance().add(amount));
    }
}
