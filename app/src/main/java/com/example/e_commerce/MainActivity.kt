package com.example.e_commerce

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val repo = Repo.getInstance(ConcreteRemoteSource, ConcreteLocalSource.getInstance(this))
//        val lineItems = listOf(
//            LineItem(quantity = 1, variant_id = 45786104496427),
//        )
//
//
//        val customer = Customer(7167160058155)
//
//        val order = Order(
//            customer = customer,
//            financial_status = "pending",
//            line_items = lineItems
//        )
//
//        val sendedOrder = SendedOrder(order = order)
////        lifecycleScope.launch {
////            repo.getCustomerOrders(7167160058155)
////        }
//
//        val variantData = VariantData(Variant(5))
//
//
//        val concreteRemoteSource = ConcreteRemoteSource
//        val inventory = InventoryItem(5, 47836051603755)
//
//
//        val inventoryLevel = InventoryLevelData(87897276715,47836051603755,5)
//
//        lifecycleScope.launch(Dispatchers.IO) {
//            concreteRemoteSource.updateInventoryLevel(inventoryLevel)
//            //concreteRemoteSource.updateInventoryItems(47836051603755, Inventory(inventory))
//            //repo.updateVariant(45786104529195,variantData)
//        }
    }
}