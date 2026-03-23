package in.musab.removebg.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayException;

import in.musab.removebg.entity.OrderEntity;
import in.musab.removebg.repository.OrderRepository;
import in.musab.removebg.service.OrderService;
import in.musab.removebg.service.RazorpayService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final RazorpayService razorpayService;
    private final OrderRepository orderRepository;

    private static final Map<String, PlanDetails> PLAN_DETAILS = Map.of(
            "Basic", new PlanDetails("Basic", 100, 499),
            "Premium", new PlanDetails("Premium", 250, 899),
            "Ultimate", new PlanDetails("Ultimate", 1000, 1499));

    private record PlanDetails(String name, int credits, int amount) {

    }

    @Override
    public Order createOrder(String planId, String clerkId) throws RazorpayException {

        PlanDetails details = PLAN_DETAILS.get(planId);
        if (details == null) {
            throw new IllegalArgumentException("Invalid plan: " + planId);
        }

        try {
            Order razorpayOrder = razorpayService.createOrder(details.amount, "INR");

            OrderEntity newOrder = OrderEntity.builder()
                    .clerkId(clerkId)
                    .plan(details.name())
                    .credits(details.credits())
                    .amount((double) details.amount())
                    .orderId(razorpayOrder.get("id"))
                    .build();
            orderRepository.save(newOrder);
            return razorpayOrder;
        } catch (RazorpayException e) {
            throw new RazorpayException("Error while creating the order", e);
        }
    }

}
