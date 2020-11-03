package Bot;

import Bot.Keyboards.InlineKeyboardMarkupBuilder;
import Bot.Keyboards.ReplyKeyboardMarkupBuilder;
import Models.Products.Accessory;
import Service.AccessoriesService;
import Service.CartService;
import Service.HookahService;
import Service.TobaccoService;
import Models.Products.Hookah;
import Models.Products.Tobacco;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private static final HookahService HOOKAH_SERVICE = new HookahService();
    private static final TobaccoService TOBACCO_SERVICE = new TobaccoService();
    private static final CartService CART_SERVICE = new CartService();
    private static final AccessoriesService ACCESSORIES_SERVICE = new AccessoriesService();
    private static final ArrayList<String> allAccessories = ACCESSORIES_SERVICE.getAllNamesList();
    private static final ArrayList<String> availableAccessories = ACCESSORIES_SERVICE.getAvailableAccessoriesNamesList();
    private static final List<String> allAccessoryTypes = ACCESSORIES_SERVICE.getAllTypes();
    private static final ArrayList<String> allHookahBrands = HOOKAH_SERVICE.getAllBrandsList();
    private static final ArrayList<String> availableBrands = HOOKAH_SERVICE.getAvailableBrandsList();
    private static final ArrayList<String> allTobaccoFortresses = TOBACCO_SERVICE.getAllFortresses();
    private static final ArrayList<String> availableFortressesRad = TOBACCO_SERVICE.getAvailableFortressesByStreet("Rad");
    private static final ArrayList<String> availableFortressesKar = TOBACCO_SERVICE.getAvailableFortressesByStreet("Kar");


    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }


    public void onUpdateReceived(Update update) {
        try {
            updateHandle(update);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public EditMessageText messageHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessageText;
        switch (text) {
            case "Наши Контакты":
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id).rebuild(mes_id);
                editMessageText.setText("г. Самара, ул. Радонежская, 1\n" +
                        "Часы работы: ПН – ВС, с 12.00 до 24.00\n" +
                        "\n" +
                        "Телефон: 8 (927) 002-75-57" +
                        "\n" +
                        "\nг. Самара, пр. Карла Маркса, 196 (ЖК Центральный)\n" +
                        "Часы работы: ПН – ВС, с 12.00 до 24.00\n" +
                        "\n" +
                        "Телефон: 8 (927) 760-11-17");
                return editMessageText;
            case "Каталог":
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Табаки", "mТабаки")
                        .button("Кальяны", "mКальяны")
                        .endRow()
                        .row()
                        .button("Акссесуары", "mАкссесуары")
                        .button("Уголь", "mУголь")
                        .endRow()
                        .row()
                        .button("Помощь", "mПомощь")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Каталог GRIZZLY SHOP*\nВыберите категорию интересующего товара:");
                return editMessageText;
            case "Помощь":
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Табаки", "mТабаки")
                        .button("Кальяны", "mКальяны")
                        .endRow()
                        .row()
                        .button("Акссесуары", "mАкссесуары")
                        .button("Уголь", "mУголь")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Каталог GRIZZLY SHOP*\n\nДля совершения заказа необходимо:\n-Выбрать интересующий товар из каталога" +
                        "\n-Добавить его в корзину\n-Оформить заказ, сформировав необходимые данные по доставке");
                return editMessageText;
            case "Наличие":
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Табаки", "mnalT")
                        .button("Кальяны", "hnal")
                        .endRow()
                        .row()
                        .button("Акссесуары", "anal")
                        .button("Уголь", "unal")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Наличие GRIZZLY SHOP*\nВыберите категорию интересующего товара:");
                return editMessageText;
            case "nalT":
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Ул. Радонежская 1", "tnalRad")
                        .endRow()
                        .row()
                        .button("Ул. Проспект Карла Маркса 196", "tnalKar")
                        .endRow()
                        .row()
                        .button("Назад", "mНаличие")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*Наличие / Табаки*\nТабаки будут сформированы по наличию вкусов в " +
                        "выбранном магазине.\nВыберите магазин:");
                return editMessageText;
            case "Кальяны":
                return hookahHandle(text, chat_id, mes_id);
            case "Табаки":
                return tobaccoHandle(text, chat_id, mes_id);
            case "Корзина":
                return cartHandle(text, chat_id, mes_id);
        }
        return InlineKeyboardMarkupBuilder.create(chat_id)
                .rebuild(mes_id).setText("*Данный функционал находится в разработке*");
    }

    public SendMessage messageStarter(String text, SendMessage sendMessage) {
        long chat_id = Long.parseLong(sendMessage.getChatId());
        switch (text) {
            case "Назад":
            case "Главное меню":
            case "/start":
                sendMessage = ReplyKeyboardMarkupBuilder.create(chat_id)
                        .setText("*Добро пожаловать в GRIZZLY SHOP!*" +
                                "\nУ нас вы найдёте большой ассортимент кальянной " +
                                "продукции, включая всевозможные аксесуары и огромный выбор табака!" +
//                                "\n[Наш сайт](https://hookahinrussia.ru/)" +
                                "\n\nИспользуйте кнопки на клавиатуре снизу для навигации")
                        .row()
                        .button("Каталог")
                        .button("Наличие")
                        .endRow()
                        .row()
                        .button("Корзина")
                        .button("Наши Контакты")
                        .endRow()
                        .build();
                break;
            case "Каталог":
                sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .setText("*Каталог GRIZZLY SHOP*\nВыберите категорию интересующего товара:")
                        .row()
                        .button("Табаки", "mТабаки")
                        .button("Кальяны", "mКальяны")
                        .endRow()
                        .row()
                        .button("Акссесуары", "mАкссесуары")
                        .button("Уголь", "mУголь")
                        .endRow()
                        .row()
                        .button("Электронные испарители", "mЭлектронные испарители")
                        .endRow()
                        .row()
                        .button("Помощь", "mПомощь")
                        .endRow()
                        .build();
                break;
            case "Наличие":
                sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .setText("*Наличие GRIZZLY SHOP*\nВыберите категорию интересующего товара:")
                        .row()
                        .button("Табаки", "mnalT")
                        .button("Кальяны", "hnal")
                        .endRow()
                        .row()
                        .button("Акссесуары", "anal")
                        .button("Уголь", "unal")
                        .endRow()
                        .row()
                        .button("Электронные испарители", "enal")
                        .endRow()
                        .build();
                break;
            case "Корзина":
                sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .setText(CART_SERVICE.getUserCart(chat_id).toString())
                        .row()
                        .button("Оформить заказ", "cОформить заказ")
                        .endRow()
                        .row()
                        .button("Очистить корзину", "cОчистить корзину")
                        .endRow()
                        .build();
                break;
            case "Наши Контакты":
                sendMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .setText("г. Самара, ул. Радонежская, 1\n" +
                                "Часы работы: ПН – ВС, с 12.00 до 24.00\n" +
                                "\n" +
                                "Телефон: 8 (927) 002-75-57" +
                                "\n" +
                                "\nг. Самара, пр. Карла Маркса, 196 (ЖК Центральный)\n" +
                                "Часы работы: ПН – ВС, с 12.00 до 24.00\n" +
                                "\n" +
                                "Телефон: 8 (927) 760-11-17")
                        .build();
                break;
        }
        return sendMessage;
    }

    public EditMessageText hookahHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessage;
        Hookah currHookah;
        if (allHookahBrands.contains(text)) {
            ArrayList<Hookah> hookahs = HOOKAH_SERVICE.getHookahsByBrand(text);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .hookahButtons(hookahs)
                    .row()
                    .button("Назад", "hКальяны")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("Товары бренда " + text + ": ");
        } else if (text.contains("crt")) {
            currHookah = HOOKAH_SERVICE.getHookahById(Long.parseLong(text.replace("crt", "")));
            CART_SERVICE.getUserCart(chat_id).getCart().add(currHookah);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("Убрать из корзины", "c" + currHookah.getId() + "delh")
                    .endRow()
                    .row()
                    .button("Назад", "hnal")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*" + currHookah.getBrand() + "* | " + currHookah.getName() +
                    "\nЦена: " + currHookah.getPrice() + " руб." +
                    "\n\n" + currHookah.getDescription().trim() +
                    "\n[_](" + currHookah.getImg() + ")");
        } else if (text.contains("id")) {
            currHookah = HOOKAH_SERVICE.getHookahById(Long.parseLong(text.replace("id", "")));
            if (currHookah.isAvailable()) {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("В корзину", "h" + currHookah.getId() + "crt")
                        .endRow()
                        .row()
                        .button("Назад", "hnal")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*" + currHookah.getBrand() + "* | " + currHookah.getName() +
                        "\nЦена: " + currHookah.getPrice() + " руб." +
                        "\n\n" + currHookah.getDescription().trim() +
                        "\n[_](" + currHookah.getImg() + ")");
            } else {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Назад", "hКальяны")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*" + currHookah.getBrand() + "* | " + currHookah.getName() + " \n_(нет в наличии)_" +
                        "\nЦена: " + currHookah.getPrice() + " руб." +
                        "\n\n" + currHookah.getDescription().trim() +
                        "\n[_](" + currHookah.getImg() + ")");
            }
        } else if (text.contains("nal")) {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(availableBrands, "havl")
                    .row()
                    .button("Назад", "mНаличие")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Наличие / Кальяны*\nВыберите бренд кальяна:");
        } else if (text.contains("avl")) {
            ArrayList<Hookah> hookahs = HOOKAH_SERVICE.getAvailableHookahsByBrand(text.replace("avl", ""));
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .hookahButtons(hookahs)
                    .row()
                    .button("Назад", "hnal")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Наличие / Кальяны*\nТовары бренда " + text + ": ");
        } else {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(allHookahBrands, "h")
                    .row()
                    .button("Назад", "mКаталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Каталог / Кальяны*\nВыберите бренд кальяна:");
        }
        return editMessage.setParseMode("Markdown");
    }

    public EditMessageText accessoryHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessageText;
        Accessory currAccessory;
        if (allAccessoryTypes.contains(text)) {
            ArrayList<Accessory> accessories = ACCESSORIES_SERVICE.getAccessoriesByType(text);
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .accessoryButtons(accessories)
                    .row()
                    .button("Назад", "aАкссесуары")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Каталог / Акссесуары*\nКатегории Акссесуаров: " + text);
        } else if (text.contains("crt")) {
            currAccessory = ACCESSORIES_SERVICE.getAccessoryById(Long.parseLong(text.replace("crt", "")));
            CART_SERVICE.getUserCart(chat_id).getCart().add(currAccessory);
            editMessageText = InlineKeyboardMarkupBuilder
                    .create(chat_id)
                    .row()
                    .button("Назад","anal")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("Товар: " + currAccessory.getName() + "\nУспешно добавлен в корзину");

        }
        else if (text.contains("id")){
            currAccessory = ACCESSORIES_SERVICE.getAccessoryById(Long.parseLong(text.replace("crt", "")));
            if (currAccessory.isAvailable()){
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("В корзину", "a" + currAccessory.getId() + "crt")
                        .endRow()
                        .row()
                        .button("Назад","anal")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*" + currAccessory.getName() + "* | " +
                        "\nЦена: " + currAccessory.getPrice() + " руб." +
                        "\n\n" + currAccessory.getDescription().trim() +
                        "\n[_](" + currAccessory.getImg() + ")");
            }
            else {
                editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("Назад","aАкссесуары")
                        .endRow()
                        .rebuild(mes_id);
                editMessageText.setText("*" + currAccessory.getName() + "* | " + " \n_(нет в наличии)_" +
                        "\nЦена: " + currAccessory.getPrice() + " руб." +
                        "\n\n" + currAccessory.getDescription().trim() +
                        "\n[_](" + currAccessory.getImg() + ")");
            }
        }
        else if (text.contains("nal")){
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons((ArrayList<String>) allAccessoryTypes,"aavl")
                    .row()
                    .button("Назад","mНаличие")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Наличие / Акссесуары*\nВыберите категорию акссесуаров:");

        }
        else if (text.contains("avl")){
            ArrayList<Accessory> accessories = ACCESSORIES_SERVICE.getAvailableAccessoriesByType(text.replace("avl", ""));
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .accessoryButtons(accessories)
                    .row()
                    .button("Назад","anal")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Наличие / Акссесуары*\nАкссесуары категории " + text + ": ");
        }
        else {
            editMessageText = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons((ArrayList<String>) allAccessoryTypes,"a")
                    .row()
                    .button("Назад","mКаталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessageText.setText("*Каталог / Акссесуары*\nВыберите категорию:");

        }
        return editMessageText.setParseMode("Markdown");
    }

    public EditMessageText tobaccoHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessage;
        Tobacco currTobacco;
        if (allTobaccoFortresses.contains(text)) {
            ArrayList<Tobacco> tobaccos = TOBACCO_SERVICE.getTobaccoByFortress(text);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .tobaccoButtons(tobaccos, "")
                    .row()
                    .button("Назад", "tТабаки")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Каталог / Табаки*\nТабаки крепости: " + text);
        } else if (text.contains("crt")) {
            if (text.contains("RRR")) {
                currTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("crtRRR", "")));
                ArrayList<String> allTastes = currTobacco.getRadonejskayaTastes();
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(allTastes, "t" + currTobacco.getId() + "&")
                        .row()
                        .button("Назад", "tid" + currTobacco.getId() + "RRR")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("Выберите интересующий вкус:");
            } else if (text.contains("KKK")) {
                currTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("crtKKK", "")));
                ArrayList<String> allTastes = currTobacco.getKarlaMarksaTastes();
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(allTastes, "t" + currTobacco.getId() + "&")
                        .row()
                        .button("Назад", "tid" + currTobacco.getId() + "KKK")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("Выберите интересующий вкус:");
            } else {
                currTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("crt", "")));
                if (currTobacco.getRadonejskayaTastes().get(0) == null && currTobacco.getKarlaMarksaTastes().get(0) == null) {
                    editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .row()
                            .button("Ул. Радонежская 1", "t" + currTobacco.getId() + "crtRRR")
                            .endRow()
                            .row()
                            .button("Ул. Проспект Карла Маркса 196", "t" + currTobacco.getId() + "crtKKK")
                            .endRow()
                            .row()
                            .button("Назад", "tid" + currTobacco.getId())
                            .endRow()
                            .rebuild(mes_id);
                    editMessage.setText("Для данного табака в наличии есть следующие вкусы:\n\n"
                            + "*ул. Радонежская 1* | `" + currTobacco.getRadonejskayaTastes().toString().replace("{", "")
                            .replace("}", "") + "`\n\n*Проспект Карла Маркса 196* | `"
                            + currTobacco.getKarlaMarksaTastes().toString().replace("{", "")
                            .replace("}", "") + "`\n\nВыберите магазин:");
                }
                else if (currTobacco.getRadonejskayaTastes().get(0) != null) {
                    ArrayList<String> allTastes = currTobacco.getRadonejskayaTastes();
                    editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .buttons(allTastes, "t" + currTobacco.getId() + "&")
                            .row()
                            .button("Назад", "tid" + currTobacco.getId() + "RRR")
                            .endRow()
                            .rebuild(mes_id);
                    editMessage.setText("Данный табак доступен по адресу *Ул. Радонежская 1*.\n\n" +
                            "Выберите интересующий вкус:");
                }
                else {
                    ArrayList<String> allTastes = currTobacco.getKarlaMarksaTastes();
                    editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .buttons(allTastes, "t" + currTobacco.getId() + "&")
                            .row()
                            .button("Назад", "tid" + currTobacco.getId() + "KKK")
                            .endRow()
                            .rebuild(mes_id);
                    editMessage.setText("Данный табак доступен по адресу *Проспект Карла Маркса 196*.\n\n" +
                            "Выберите интересующий вкус:");
                }
            }
        } else if (text.contains("&")) {
            String[] arr = text.split("&");
            currTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(arr[0]));
            currTobacco.setTaste(arr[1]);
            CART_SERVICE.getUserCart(chat_id).getCart().add(currTobacco);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("Убрать из корзины", "c" + currTobacco.getId() + "delt")
                    .endRow()
                    .row()
                    .button("Назад", "tnal")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*" + currTobacco.getName() + "*" +
                    "\nЦена: " + currTobacco.getPrice() + " руб." +
                    "\n\n" + currTobacco.getDescription().trim() +
                    "\n[_](" + currTobacco.getImg() + ")");
        } else if (text.contains("id")) {
            if (text.contains("RRR")) {
                currTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("id", "").replace("RRR", "")));
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("В корзину", "t" + currTobacco.getId() + "crtRRR")
                        .endRow()
                        .row()
                        .button("Назад", "tavlbRad")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*" + currTobacco.getName() + "*" +
                        "\nЦена: " + currTobacco.getPrice() + " руб." +
                        "\n\n" + currTobacco.getDescription().trim() +
                        "\n[_](" + currTobacco.getImg() + ")");
            } else if (text.contains("KKK")) {
                Tobacco t = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("id", "").replace("KKK", "")));
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("В корзину", "t" + t.getId() + "crtKKK")
                        .endRow()
                        .row()
                        .button("Назад", "tavlbKar")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*" + t.getName() + "*" +
                        "\nЦена: " + t.getPrice() + " руб." +
                        "\n\n" + t.getDescription().trim() +
                        "\n[_](" + t.getImg() + ")");
            } else {
                Tobacco t = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("id", "")));
                if (t.isAvailable()) {
                    editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .row()
                            .button("В корзину", "t" + t.getId() + "crt")
                            .endRow()
                            .row()
                            .button("Назад", "tТабаки")
                            .endRow()
                            .rebuild(mes_id);
                    editMessage.setText("*" + t.getName() + "*" +
                            "\nЦена: " + t.getPrice() + " руб." +
                            "\n\n" + t.getDescription().trim() +
                            "\n[_](" + t.getImg() + ")");
                } else {
                    editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                            .row()
                            .button("Назад", "tТабаки")
                            .endRow()
                            .rebuild(mes_id);
                    editMessage.setText("*" + t.getName() + "* \n_(нет в наличии)_" +
                            "\n\nЦена: " + t.getPrice() + " руб." +
                            "\n\n" + t.getDescription().trim() +
                            "\n[_](" + t.getImg() + ")");
                }
            }
        } else if (text.contains("nal")) {
            if (text.contains("Rad")) {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(availableFortressesRad, "tavlbRad")
                        .row()
                        .button("Назад", "mnalT")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*Наличие / Табаки / Радонежская 1*\nВыберите крепость табака:");
            } else {
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .buttons(availableFortressesKar, "tavlbKar")
                        .row()
                        .button("Назад", "mnalT")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*Наличие / Табаки / Проспект Карла Маркса 196*\nВыберите крепость табака:");
            }
        } else if (text.contains("avlb")) {
            ArrayList<Tobacco> avlTobacco;
            if (text.contains("Rad")) {
                avlTobacco = TOBACCO_SERVICE.getAvailableTobaccoByFortress(text.replace("avlbRad", ""), "Rad");
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .tobaccoButtons(avlTobacco, "RRR")
                        .row()
                        .button("Назад", "tnalRad")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*Наличие / Табаки / Радонежская 1*\nТабаки крепости: " + text);
            } else {
                avlTobacco = TOBACCO_SERVICE.getAvailableTobaccoByFortress(text.replace("avlbKar", ""), "Kar");
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .tobaccoButtons(avlTobacco, "KKK")
                        .row()
                        .button("Назад", "tnalKar")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*Наличие / Табаки / Проспект Карла Маркса 196*\nТабаки крепости: " + text);
            }
        } else {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .buttons(allTobaccoFortresses, "t")
                    .row()
                    .button("Назад", "mКаталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("*Каталог / Табаки*\nВыберите крепость табака:");
        }
        return editMessage;
    }

    public EditMessageText cartHandle(String text, long chat_id, long mes_id) {
        text = text.substring(1);
        EditMessageText editMessage;
        if (text.contains("del")) {
            if (text.contains("delh")) {
                Hookah currHookah = HOOKAH_SERVICE.getHookahById(Long.parseLong(text.replace("delh","")));
                CART_SERVICE.deleteFromCart(currHookah, chat_id);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("В корзину", "h" + currHookah.getId() + "crt")
                        .endRow()
                        .row()
                        .button("Назад", "hnal")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*" + currHookah.getBrand() + "* | " + currHookah.getName() +
                        "\nЦена: " + currHookah.getPrice() + " руб." +
                        "\n\n" + currHookah.getDescription().trim() +
                        "\n[_](" + currHookah.getImg() + ")");
                return editMessage;
            }
            else if (text.contains("delt")) {
                Tobacco currTobacco = TOBACCO_SERVICE.getTobaccoById(Long.parseLong(text.replace("delt","")));
                CART_SERVICE.deleteFromCart(currTobacco, chat_id);
                editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                        .row()
                        .button("В корзину", "c" + currTobacco.getId() + "crt")
                        .endRow()
                        .row()
                        .button("Назад", "tnal")
                        .endRow()
                        .rebuild(mes_id);
                editMessage.setText("*" + currTobacco.getName() + "*" +
                        "\nЦена: " + currTobacco.getPrice() + " руб." +
                        "\n\n" + currTobacco.getDescription().trim() +
                        "\n[_](" + currTobacco.getImg() + ")");
            }
            else if (text.contains("dela")) {
                Accessory currAccessory = ACCESSORIES_SERVICE.getAccessoryById(Long.parseLong(text.replace("dela","")));
                CART_SERVICE.deleteFromCart(currAccessory, chat_id);
            }
        }
        if (text.equals("Корзина")) {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText(CART_SERVICE.getUserCart(chat_id).toString())
                    .row()
                    .button("Очистить корзину", "cОчистить корзину")
                    .endRow()
                    .row()
                    .button("Назад", "mКаталог")
                    .endRow()
                    .rebuild(mes_id);
        } else if (text.equals("Очистить корзину")) {
            CART_SERVICE.clearUserCart(chat_id);
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .row()
                    .button("Вернуться к покупкам", "mКаталог")
                    .endRow()
                    .rebuild(mes_id);
            editMessage.setText("Ваша корзина успешно очищена!");
        }
        else {
            editMessage = InlineKeyboardMarkupBuilder.create(chat_id)
                    .setText("Оформление заказа\nТут что-то должно быть")
                    .row()
                    .button("Назад", "cКорзина")
                    .endRow()
                    .rebuild(mes_id);
        }
        return editMessage;
    }

    public synchronized AnswerCallbackQuery answerCallbackQuery(String callbackId, String message) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setCallbackQueryId(callbackId);
        answer.setShowAlert(false);
        answer.setText(message);
        answer.setShowAlert(true);
        return answer;
    }

    public void updateHandle(Update update) throws TelegramApiException {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message inMessage = update.getMessage();
            long chat_id = inMessage.getChatId();
            SendMessage outMessage = new SendMessage().setChatId(chat_id);
            String text = update.getMessage().getText();
            System.out.println(new Date() + ": " + inMessage.getFrom().getFirstName() + " " +
                    inMessage.getFrom().getLastName() + " (" + inMessage.getFrom().getUserName() +
                    "): " + text);
            execute(messageStarter(text, outMessage).setParseMode("Markdown"));
        } else if (update.hasCallbackQuery()) {
            Message inMessage = update.getCallbackQuery().getMessage();
            long chat_id = inMessage.getChatId();
            long mes_id = inMessage.getMessageId();
            String text = update.getCallbackQuery().getData();
            System.out.println(new Date() + ": " + update.getCallbackQuery().getFrom().getFirstName() + " " +
                    update.getCallbackQuery().getFrom().getLastName() + " (" + update.getCallbackQuery().getFrom().getUserName() +
                    "): " + text);
            if (text.startsWith("c")) {
                if (text.contains("crt"))
                    execute(answerCallbackQuery(update.getCallbackQuery().getId(), "Товар успешно добавлен в корзину!"));
                if (text.contains("del"))
                    execute(answerCallbackQuery(update.getCallbackQuery().getId(), "Товар успешно удалён из корзины!"));
                execute(cartHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            }
            else if (text.startsWith("h")) {
                if (text.contains("crt"))
                    execute(answerCallbackQuery(update.getCallbackQuery().getId(), "Товар успешно добавлен в корзину!"));
                if (text.contains("del"))
                    execute(answerCallbackQuery(update.getCallbackQuery().getId(), "Товар успешно удалён из корзины!"));
                execute(hookahHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            }
            else if (text.startsWith("t")) {
                if (text.contains("&"))
                    execute(answerCallbackQuery(update.getCallbackQuery().getId(), "Товар успешно добавлен в корзину!"));
                if (text.contains("del"))
                    execute(answerCallbackQuery(update.getCallbackQuery().getId(), "Товар успешно удалён из корзины!"));
                execute(tobaccoHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            }
            else if (text.startsWith("m")) {
                if (text.contains("crt"))
                    execute(answerCallbackQuery(update.getCallbackQuery().getId(), "Товар успешно добавлен в корзину!"));
                if (text.contains("del"))
                    execute(answerCallbackQuery(update.getCallbackQuery().getId(), "Товар успешно удалён из корзины!"));
                execute(messageHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            }
            else if (text.startsWith("a")) {
                if (text.contains("crt"))
                    execute(answerCallbackQuery(update.getCallbackQuery().getId(), "Товар успешно добавлен в корзину!"));
                if (text.contains("del"))
                    execute(answerCallbackQuery(update.getCallbackQuery().getId(), "Товар успешно удалён из корзины!"));
                execute(accessoryHandle(text, chat_id, mes_id).setParseMode("Markdown"));
            }

        }
    }

    public String getBotUsername() {
        return "Grizzly_Shop_Bot";
    }

    public String getBotToken() {
        return "1238275097:AAGnnDKQpxjxCfjey6zxx0kVgm1EdDNMvak";
    }
}
