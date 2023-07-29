package com.example.e_commerce.model.repo

import com.data.source.FakeLocaleSource
import com.data.source.FakeRemoteSource
import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.Image
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.pojo.address.AddressResponse
import com.example.e_commerce.model.pojo.address.SendAddress
import com.example.e_commerce.model.pojo.address.SendAddressDTO
import com.example.e_commerce.model.pojo.customer.CustomerData
import com.example.e_commerce.model.pojo.customer_modified_response.CustomerModifiedResponse
import com.example.e_commerce.model.pojo.customer_modified_response.CustomerResponseData
import com.example.e_commerce.model.pojo.customer_order_response.CustomerOrderResponse
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import com.example.e_commerce.model.pojo.customer_resposnse.SmsMarketingConsent
import com.example.e_commerce.model.pojo.draftorder.response.DraftOrder
import com.example.e_commerce.model.pojo.draftorder.response.DraftResponse
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftOrder
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftRequest
import com.example.e_commerce.model.pojo.level.InventoryLevelData
import com.example.e_commerce.model.pojo.levelResponse.InventoryLevel
import com.example.e_commerce.model.pojo.levelResponse.InventoryLevelResponse
import com.example.e_commerce.model.pojo.order.OrderData
import com.example.e_commerce.model.pojo.order_details_response.CurrentSubtotalPriceSet
import com.example.e_commerce.model.pojo.order_details_response.CurrentTotalDiscountsSet
import com.example.e_commerce.model.pojo.order_details_response.CurrentTotalPriceSet
import com.example.e_commerce.model.pojo.order_details_response.CurrentTotalTaxSet
import com.example.e_commerce.model.pojo.order_details_response.Customer
import com.example.e_commerce.model.pojo.order_details_response.DefaultAddress
import com.example.e_commerce.model.pojo.order_details_response.EmailMarketingConsent
import com.example.e_commerce.model.pojo.order_details_response.OrderDetailsResponse
import com.example.e_commerce.model.pojo.order_details_response.PresentmentMoney
import com.example.e_commerce.model.pojo.order_details_response.ShopMoney
import com.example.e_commerce.model.pojo.order_details_response.SubtotalPriceSet
import com.example.e_commerce.model.pojo.order_details_response.TotalDiscountsSet
import com.example.e_commerce.model.pojo.order_details_response.TotalLineItemsPriceSet
import com.example.e_commerce.model.pojo.order_details_response.TotalPriceSet
import com.example.e_commerce.model.pojo.order_details_response.TotalShippingPriceSet
import com.example.e_commerce.model.pojo.order_details_response.TotalTaxSet
import com.example.e_commerce.model.pojo.order_response.Money
import com.example.e_commerce.model.pojo.order_response.MoneySet
import com.example.e_commerce.model.pojo.order_response.Order
import com.example.e_commerce.model.pojo.order_response.OrderResponse
import com.example.e_commerce.model.pojo.pricerule.PriceRuleResponse
import com.example.e_commerce.model.pojo.product_details.Product
import com.example.e_commerce.model.pojo.product_details.ProductDetailsResponse
import com.example.e_commerce.services.db.LocalSource
import com.example.e_commerce.services.network.RemoteSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class RepoTest {
    private val testString = "AndTeam1"

    private val productsResponse = ProductsResponse(listOf())
    private val brandsResponse = BrandsResponse(listOf())
    private val priceRuleResponse = PriceRuleResponse(listOf())
    private val productDetailsResponse = ProductDetailsResponse(
        Product(
            "", "", "", "", 0, Image(
                "", "", "", 0, 0, 0, 0, "", "", listOf(), 0,
            ), listOf(), listOf(), "", "", "", "", "", "", "", "", listOf(), ""
        )
    )
    private val customerResponse = CustomerResponse(listOf())
    private val customerModifiedResponse = CustomerModifiedResponse(
        CustomerResponseData(
            0, "", "", "", "", "", listOf(), "", true
        )
    )
    private val addressResponseData = AddressResponse(listOf())
    private val orderResponse = OrderResponse(
        Order(
            0,
            "",
            0,
            "",
            true,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            true,
            "",
            "",
            "",
            "",
            MoneySet(Money("", ""), Money("", "")),
            null,
            "",
            MoneySet(Money("", ""), Money("", "")),
            null,
            "",
            MoneySet(Money("", ""), Money("", "")),
            "",
            MoneySet(Money("", ""), Money("", "")),
            "",
            "",
            listOf(),
            "",
            true,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            listOf(),
            0,
            0,
            "",
            null,
            null,
            listOf(),
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            MoneySet(Money("", ""), Money("", "")),
            "",
            true,
            listOf(),
            false,
            false,
            "",
            "",
            MoneySet(Money("", ""), Money("", "")),
            "",
            MoneySet(Money("", ""), Money("", "")),
            "",
            "",
            MoneySet(Money("", ""), Money("", "")),
            "",
            null,
            "",
            MoneySet(Money("", ""), Money("", "")),
            "",
            0,
            "",
            null,
            "",
            listOf()
        )
    )
    private val customerOrderResponse = CustomerOrderResponse(listOf())
    private val orderDetailsResponse = OrderDetailsResponse(
        com.example.e_commerce.model.pojo.order_details_response.Order(

            id = 5416176288043,
            admin_graphql_api_id = "gid://shopify/Order/5416176288043",
            app_id = 50840895489,
            buyer_accepts_marketing = false,
            contact_email = "dody012800091892651999@gmail.com",
            created_at = "2023-07-29T08:16:18-04:00",
            currency = "EGP",
            current_subtotal_price = "90.00",
            current_subtotal_price_set = CurrentSubtotalPriceSet(
                PresentmentMoney("", ""),
                ShopMoney("", "")
            ),
            current_total_discounts = "0.00",
            current_total_discounts_set = CurrentTotalDiscountsSet(
                PresentmentMoney("", ""),
                ShopMoney("", "")
            ),
            current_total_price = "90.00",
            current_total_price_set = CurrentTotalPriceSet(
                PresentmentMoney("", ""),
                ShopMoney("", "")
            ),
            current_total_tax = "0.00",
            current_total_tax_set = CurrentTotalTaxSet(PresentmentMoney("", ""), ShopMoney("", "")),
            email = "dody012800091892651999@gmail.com",
            financial_status = "paid",
            name = "#1095",
            note = "",
            order_number = 1095,
            order_status_url = "https://itp-sv-and1.myshopify.com/79670411563/orders/47b0685d34287b7b8a462c6fa4721995/authenticate?key=cd1b3317092517319def0018a3e4c9ca",
            presentment_currency = "EGP",
            processed_at = "2023-07-29T08:16:18-04:00",
            subtotal_price = "90.00",
            subtotal_price_set = SubtotalPriceSet(PresentmentMoney("", ""), ShopMoney("", "")),
            tags = "",
            total_discounts = "0.00",
            total_discounts_set = TotalDiscountsSet(PresentmentMoney("", ""), ShopMoney("", "")),
            total_line_items_price = "90.00",
            total_line_items_price_set = TotalLineItemsPriceSet(
                PresentmentMoney("", ""),
                ShopMoney("", "")
            ),
            total_outstanding = "90.00",
            total_price = "90.00",
            total_price_set = TotalPriceSet(PresentmentMoney("", ""), ShopMoney("", "")),
            total_tax = "0.00",
            total_tax_set = TotalTaxSet(PresentmentMoney("", ""), ShopMoney("", "")),
            total_tip_received = "0.00",
            total_weight = 0,
            updated_at = "2023-07-29T08:17:19-04:00",
            token = "47b0685d34287b7b8a462c6fa4721995",
            customer = Customer(
                id = 7175207026987,
                email = "dody012800091892651999@gmail.com",
                accepts_marketing = false,
                created_at = "2023-07-26T07:44:30-04:00",
                updated_at = "2023-07-29T08:16:18-04:00",
                first_name = "mohamed ali",
                last_name = "",
                state = "disabled",
                note = "1128980087083",
                verified_email = true,
                accepts_marketing_updated_at = "",
                admin_graphql_api_id = "",
                currency = "",
                default_address = DefaultAddress(
                    id = 9312505299243,
                    customer_id = 7175207026987,
                    first_name = "Mohamed",
                    last_name = "Ali",
                    company = "And Team1",
                    address1 = "34 Yehia Ibrahim, Mohammed Mazhar, Zamalek, Cairo Governorate 11411, Egypt",
                    address2 = "",
                    city = "Cairo Governorate",
                    province = "",
                    country = "Egypt",
                    zip = "3753450",
                    phone = "01280009189",
                    name = "Mohamed Ali",
                    province_code = "",
                    country_code = "EG",
                    country_name = "Egypt",
                    default = true,
                ), email_marketing_consent = EmailMarketingConsent("", "", ""),
                marketing_opt_in_level = "",
                multipass_identifier = "",
                phone = "",
                sms_marketing_consent = "",
                tags = "",
                tax_exempt = false,
                tax_exemptions = listOf()
            ),
            line_items = listOf(),
            billing_address = "",
            browser_ip = "",
            cancel_reason = "",
            cancelled_at = "",
            cart_token = "",
            checkout_id = "",
            checkout_token = "",
            client_details = "",
            closed_at = "",
            company = "",
            confirmed = false,
            current_total_additional_fees_set = "",
            current_total_duties_set = "",
            customer_locale = "",
            device_id = "",
            discount_applications = listOf(),
            discount_codes = listOf(),
            estimated_taxes = false,
            fulfillment_status = "",
            fulfillments = listOf(),
            landing_site = "",
            landing_site_ref = "",
            location_id = "",
            merchant_of_record_app_id = "",
            note_attributes = listOf(),
            number = 0, original_total_additional_fees_set = "", original_total_duties_set = "",
            phone = "",
            payment_gateway_names = listOf(),
            payment_terms = "",
            reference = "",
            referring_site = "",
            refunds = listOf(),
            shipping_address = "",
            source_identifier = "",
            source_name = "",
            source_url = "",
            total_shipping_price_set = TotalShippingPriceSet(
                PresentmentMoney("", ""),
                ShopMoney("", "")
            ),
            shipping_lines = listOf(),
            tax_lines = listOf(),
            test = false,
            taxes_included = false, user_id = ""
        )
    )

    private val inventoryLevelResponse = InventoryLevelResponse(InventoryLevel("", 0, 0, 0, ""))
    private val draftResponse = DraftResponse(
        DraftOrder(
            com.example.e_commerce.model.pojo.customer_resposnse.Customer(
                id = 7175207026987,
                email = "dody012800091892651999@gmail.com",
                accepts_marketing = false,
                created_at = "2023-07-26T07:44:30-04:00",
                updated_at = "2023-07-29T08:16:18-04:00",
                first_name = "mohamed ali",
                last_name = "",
                state = "disabled",
                note = "1128980087083",
                verified_email = true,
                accepts_marketing_updated_at = "",
                admin_graphql_api_id = "",
                currency = "",
                default_address = com.example.e_commerce.model.pojo.customer_resposnse.DefaultAddress(
                    id = 9312505299243,
                    customer_id = 7175207026987,
                    first_name = "Mohamed",
                    last_name = "Ali",
                    company = "And Team1",
                    address1 = "34 Yehia Ibrahim, Mohammed Mazhar, Zamalek, Cairo Governorate 11411, Egypt",
                    address2 = "",
                    city = "Cairo Governorate",
                    province = "",
                    country = "Egypt",
                    zip = "3753450",
                    phone = "01280009189",
                    name = "Mohamed Ali",
                    province_code = "",
                    country_code = "EG",
                    country_name = "Egypt",
                    default = true,
                ), email_marketing_consent = com.example.e_commerce.model.pojo.customer_resposnse.EmailMarketingConsent("", "", ""),
                marketing_opt_in_level = "",
                multipass_identifier = "",
                phone = "",
                sms_marketing_consent = SmsMarketingConsent("", "", "", ""),
                tags = "",
                tax_exempt = false,
                tax_exemptions = listOf(),
                addresses = listOf(),
                last_order_id = "",
                last_order_name = "",
                total_spent = "", orders_count = 0
            ), "", 0, listOf(), ""
        )
    )

    private lateinit var localSource: LocalSource
    private lateinit var remoteSource: RemoteSource
    private lateinit var repo: RepoInterface

    @Before
    fun setUp() {

        localSource = FakeLocaleSource(testString)
        remoteSource = FakeRemoteSource(
            productsResponse,
            brandsResponse,
            priceRuleResponse,
            productDetailsResponse,
            customerResponse,
            customerModifiedResponse,
            addressResponseData,
            orderResponse,
            customerOrderResponse,
            orderDetailsResponse,
            inventoryLevelResponse,
            draftResponse
            )
        repo = Repo.getInstance(remoteSource,localSource)
    }

    @Test
    fun getAllProducts() = runTest{
        //when
        val result = repo.getAllProducts()

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(productsResponse, it.body())
            }
        }
    }

    @Test
    fun getBrands() = runTest{
        //when
        val result = repo.getBrands()

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(brandsResponse, it.body())
            }
        }
    }

    @Test
    fun getProductsById()= runTest{
        //when
        val result = repo.getProductsById(0)

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(productsResponse, it.body())
            }
        }
    }

    @Test
    fun getProductById() = runTest{
        //when
        val result = repo.getProductById(0)

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(productDetailsResponse, it.body())
            }
        }
    }

    @Test
    fun createCustomer() = runTest{
        //when
        val result = repo.createCustomer(customerData = CustomerData(com.example.e_commerce.model.pojo.customer.Customer(
            listOf(),"","","","",false,"",""
        )))

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(customerResponse, it.body())
            }
        }
    }

    @Test
    fun getCustomerByEmailAndName()= runTest{
        //when
        val result = repo.getCustomerByEmailAndName("","")

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(customerResponse, it.body())
            }
        }
    }

    @Test
    fun modifyCustomer() = runTest{
        //when
        val result = repo.modifyCustomer(0,CustomerData(com.example.e_commerce.model.pojo.customer.Customer(
            listOf(),"","","","",false,"",""
        )))

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(customerModifiedResponse, it.body())
            }
        }
    }

    @Test
    fun getAllPricesRules() = runTest{
        //when
        val result = repo.getAllPricesRules()

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(priceRuleResponse, it.body())
            }
        }
    }

    @Test
    fun getAddressesForCustomer()= runTest{
        //when
        val result = repo.getAddressesForCustomer("")

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(addressResponseData, it.body())
            }
        }
    }

    @Test
    fun createAddressForCustomer()= runTest{
        //when
        val result = repo.createAddressForCustomer("0", SendAddressDTO(SendAddress("","","","","","","","","","","")))

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(addressResponseData, it.body())
            }
        }
    }

    @Test
    fun makeAddressDefault()= runTest{
        //when
        val result = repo.makeAddressDefault("","")

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(addressResponseData, it.body())
            }
        }
    }

    @Test
    fun createOrder()= runTest{
        //when
        val result = repo.createOrder(OrderData(com.example.e_commerce.model.pojo.order.Order("",
            listOf(), listOf(),false,""
        )))

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(orderResponse, it.body())
            }
        }
    }

    @Test
    fun getCustomerOrders()= runTest{
        //when
        val result = repo.getCustomerOrders(0)

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(customerOrderResponse, it.body())
            }
        }
    }

    @Test
    fun getOrderById()= runTest{
        //when
        val result = repo.getOrderById(0)

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(orderDetailsResponse, it.body())
            }
        }
    }

    @Test
    fun updateInventoryLevel()= runTest{
        //when
        val result = repo.updateInventoryLevel(InventoryLevelData(0,0,0))

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(inventoryLevelResponse, it.body())
            }
        }
    }

    @Test
    fun writeStringToSettingSP() {
        //when
        repo.writeStringToSettingSP("val1", "AndTeam1")
        val result = repo.readStringFromSettingSP("val1")

        //then
        assertEquals("AndTeam1",result)
    }

    @Test
    fun readStringFromSettingSP() {
        //when
        val result = repo.readStringFromSettingSP("val1")

        //then
        assertEquals("AndTeam1",result)
    }

    @Test
    fun createDraftOrder() = runTest{
        //when
        val result = repo.createDraftOrder(SendDraftRequest(SendDraftOrder(listOf(),"","")))

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(draftResponse, it.body())
            }
        }
    }

    @Test
    fun modifyDraftOrder() = runTest{
        //when
        val result = repo.modifyDraftOrder(2,SendDraftRequest(SendDraftOrder(listOf(),"","")))

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(draftResponse, it.body())
            }
        }
    }

    @Test
    fun getDraftOrderByDraftId()= runTest{
        //when
        val result = repo.getDraftOrderByDraftId(1)

        //then
        result.collectLatest {
            if(it.isSuccessful){
                assertEquals(draftResponse, it.body())
            }
        }
    }
}