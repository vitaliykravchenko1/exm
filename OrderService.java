import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OrderProcessor {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private NotificationService notificationService;

    public void processOrder(Order order) {
        if (order.getItems() != null && order.getItems().size() > 0) {
            double total = 0;
            for (Item item : order.getItems()) {
                total += item.getPrice() * item.getQuantity();
            }

            if (total > 100) {
                order.setDiscount(10);
            } else {
                order.setDiscount(0);
            }

            order.setTotal(total - order.getDiscount());
            notificationService.notifyOrderCreated(order); // http call with avg latency 5 seconds
            saveOrder(order);
        }
    }

    @Transactional
    public void saveOrder(Order order) {
        if (order.getCustomer() != null) {
            customerRepository.save(order.getCustomer());
        }
        
        orderRepository.save(order);
    }

}

interface Item {
    double getPrice();

    int getQuantity();
}

interface Order {
    List<Item> getItems();

    void setItems(List<Item> items);
    
    Customer getCustomer();

    double getDiscount();

    void setDiscount(double discount);

    double getTotal();

    void setTotal(double total);

}

interface Customer {
    String getName();
}


@Repository
class OrderRepository {
    public void save(Order order) {
        System.out.println("Order saved: total = " + order.getTotal());
    }
}

@Repository
class CustomerRepository {
    public void save(Customer customer) {
        System.out.println("Customer saved: " + customer);
    }
}

@Service
class NotificationService {
    public void notifyOrderCreated(Order order) {
        System.out.println("Notification: order created with total = " + order.getTotal());
    }
}
