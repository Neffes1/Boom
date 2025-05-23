package org.example;

import java.time.LocalDate;
import java.util.*;

// Модель пользователя
class User {
    private String username;
    private String password;
    private String role;
    private String fullName;
    private String birthDate;
    private String snils;

    public User(String username, String password, String role, String fullName, String birthDate, String snils) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.snils = snils;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
    public String getBirthDate() { return birthDate; }
    public String getSnils() { return snils; }
}

// Модель кандидата
class Candidate {
    private String id;
    private String name;
    private String party;
    private String bio;

    public Candidate(String id, String name, String party, String bio) {
        this.id = id;
        this.name = name;
        this.party = party;
        this.bio = bio;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getParty() { return party; }
    public String getBio() { return bio; }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, party);
    }
}

// Модель голосования
class Voting {
    private String id;
    private String title;
    private LocalDate endDate;
    private List<String> candidateIds;
    private Map<String, Integer> votes;
    private Set<String> votedUsers;

    public Voting(String id, String title, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.endDate = endDate;
        this.candidateIds = new ArrayList<>();
        this.votes = new HashMap<>();
        this.votedUsers = new HashSet<>();
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public LocalDate getEndDate() { return endDate; }
    public List<String> getCandidateIds() { return candidateIds; }
    public Map<String, Integer> getVotes() { return votes; }

    public void addCandidate(String candidateId) {
        if (!candidateIds.contains(candidateId)) {
            candidateIds.add(candidateId);
            votes.put(candidateId, Integer.valueOf(0));
        }
    }

    public boolean vote(String candidateId, String username) {
        if (votedUsers.contains(username) || !candidateIds.contains(candidateId)) {
            return false;
        }
        votes.put(candidateId, votes.getOrDefault(candidateId, 0) + 1);
        votedUsers.add(username);
        return true;
    }

    public boolean hasUserVoted(String username) {
        return votedUsers.contains(username);
    }

    public boolean isActive() {
        return LocalDate.now().isBefore(endDate) || LocalDate.now().isEqual(endDate);
    }
}

// Основная модель системы
class VotingSystem {
    private List<User> users;
    private List<Candidate> candidates;
    private List<Voting> votings;
    private User currentUser;

    public VotingSystem() {
        this.users = new ArrayList<>();
        this.candidates = new ArrayList<>();
        this.votings = new ArrayList<>();
        // Добавляем тестового администратора
        users.add(new User("sysadmin", "SecurePass123!", "admin", "Главный Администратор", "15.03.1985", "123-456-789 01"));
        // Добавляем тестового пользователя
        users.add(new User("citizen1", "Vote2023*", "user", "Петров Сергей Николаевич", "22.10.1992", "234-567-890 12"));
        // Добавляем тестового ЦИК
        users.add(new User("election_com", "CentralElection#1", "cec", "Центральная Избирательная Комиссия", "", ""));
        // Добавляем еще одного пользователя
        users.add(new User("voter2", "MySecretPass99", "user", "Иванова Мария Владимировна", "05.07.1988", "345-678-901 23"));
    }

    // Аутентификация
    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Методы для администратора
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public boolean deleteUser(String username) {
        if (username.equals(currentUser.getUsername())) {
            return false; // Нельзя удалить себя
        }
        return users.removeIf(user -> user.getUsername().equals(username));
    }

    public boolean createUser(String username, String password, String role, String fullName, String birthDate, String snils) {
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            return false;
        }
        users.add(new User(username, password, role, fullName, birthDate, snils));
        return true;
    }

    // Методы для ЦИК
    public String createVoting(String title, LocalDate endDate) {
        String id = "vote_" + System.currentTimeMillis();
        votings.add(new Voting(id, title, endDate));
        return id;
    }

    public boolean addCandidateToVoting(String votingId, String candidateId) {
        Voting voting = getVotingById(votingId);
        if (voting != null && getCandidateById(candidateId) != null) {
            voting.addCandidate(candidateId);
            return true;
        }
        return false;
    }

    // Методы для кандидатов
    public String createCandidate(String name, String party, String bio) {
        String id = "cand_" + System.currentTimeMillis();
        candidates.add(new Candidate(id, name, party, bio));
        return id;
    }

    public List<Candidate> getAllCandidates() {
        return new ArrayList<>(candidates);
    }

    public boolean deleteCandidate(String candidateId) {
        return candidates.removeIf(c -> c.getId().equals(candidateId));
    }

    // Методы для голосования
    public List<Voting> getActiveVotings() {
        List<Voting> active = new ArrayList<>();
        for (Voting voting : votings) {
            if (voting.isActive()) {
                active.add(voting);
            }
        }
        return active;
    }

    public Voting getVotingById(String id) {
        for (Voting voting : votings) {
            if (voting.getId().equals(id)) {
                return voting;
            }
        }
        return null;
    }

    public Candidate getCandidateById(String id) {
        for (Candidate candidate : candidates) {
            if (candidate.getId().equals(id)) {
                return candidate;
            }
        }
        return null;
    }

    public boolean vote(String votingId, String candidateId, String username) {
        Voting voting = getVotingById(votingId);
        return voting != null && voting.vote(candidateId, username);
    }
}

// Интерфейс пользователя
class VotingUI {
    private Scanner scanner;
    private VotingSystem system;

    public VotingUI(VotingSystem system) {
        this.scanner = new Scanner(System.in);
        this.system = system;
    }

    public void start() {
        while (true) {
            System.out.println("\n=== Система электронного голосования ===");
            System.out.println("1. Вход");
            System.out.println("2. Выход");
            String choice = input("Выберите действие");

            switch (choice) {
                case "1":
                    loginMenu();
                    break;
                case "2":
                    System.out.println("До свидания!");
                    return;
                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }

    private void loginMenu() {
        String username = input("Логин");
        String password = input("Пароль");

        if (system.login(username, password)) {
            User user = system.getCurrentUser();
            System.out.println("Добро пожаловать, " + user.getFullName() + "!");

            switch (user.getRole()) {
                case "admin":
                    adminMenu();
                    break;
                case "cec":
                    cecMenu();
                    break;
                case "user":
                    userMenu();
                    break;
                default:
                    System.out.println("Неизвестная роль пользователя!");
            }
            system.logout();
        } else {
            System.out.println("Неверный логин или пароль!");
        }
    }

    private void adminMenu() {
        while (true) {
            System.out.println("\n=== Меню администратора ===");
            System.out.println("1. Просмотр пользователей");
            System.out.println("2. Удалить пользователя");
            System.out.println("3. Создать пользователя");
            System.out.println("0. Выход");

            String choice = input("Выберите действие");
            switch (choice) {
                case "1":
                    listUsers();
                    break;
                case "2":
                    deleteUser();
                    break;
                case "3":
                    createUser();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }

    private void cecMenu() {
        while (true) {
            System.out.println("\n=== Меню ЦИК ===");
            System.out.println("1. Создать голосование");
            System.out.println("2. Добавить кандидата в голосование");
            System.out.println("3. Создать кандидата");
            System.out.println("4. Просмотреть кандидатов");
            System.out.println("5. Удалить кандидата");
            System.out.println("0. Выход");

            String choice = input("Выберите действие");
            switch (choice) {
                case "1":
                    createVoting();
                    break;
                case "2":
                    addCandidateToVoting();
                    break;
                case "3":
                    createCandidate();
                    break;
                case "4":
                    listCandidates();
                    break;
                case "5":
                    deleteCandidate();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }

    private void userMenu() {
        while (true) {
            System.out.println("\n=== Меню пользователя ===");
            System.out.println("1. Активные голосования");
            System.out.println("2. Проголосовать");
            System.out.println("0. Выход");

            String choice = input("Выберите действие");
            switch (choice) {
                case "1":
                    listActiveVotings();
                    break;
                case "2":
                    vote();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }

    // Методы для работы с пользователями
    private void listUsers() {
        System.out.println("\nСписок пользователей:");
        List<User> users = system.getAllUsers();
        for (User user : users) {
            System.out.printf("%s (%s) - %s%n",
                    user.getUsername(), user.getFullName(), user.getRole());
        }
    }

    private void deleteUser() {
        String username = input("Введите логин пользователя для удаления");
        if (system.deleteUser(username)) {
            System.out.println("Пользователь удален!");
        } else {
            System.out.println("Не удалось удалить пользователя!");
        }
    }

    private void createUser() {
        System.out.println("\nСоздание нового пользователя:");
        String username = input("Логин");
        String password = input("Пароль");
        String role = input("Роль (admin/cec/user)");
        String fullName = input("Полное имя");
        String birthDate = input("Дата рождения (ДД.ММ.ГГГГ)");
        String snils = input("СНИЛС");

        if (system.createUser(username, password, role, fullName, birthDate, snils)) {
            System.out.println("Пользователь создан!");
        } else {
            System.out.println("Не удалось создать пользователя (возможно, логин занят)");
        }
    }

    // Методы для работы с голосованиями
    private void createVoting() {
        String title = input("Название голосования");
        String dateStr = input("Дата окончания (ГГГГ-ММ-ДД)");
        try {
            LocalDate endDate = LocalDate.parse(dateStr);
            String id = system.createVoting(title, endDate);
            System.out.println("Голосование создано! ID: " + id);
        } catch (Exception e) {
            System.out.println("Ошибка формата даты!");
        }
    }

    private void addCandidateToVoting() {
        String votingId = input("ID голосования");
        String candidateId = input("ID кандидата");
        if (system.addCandidateToVoting(votingId, candidateId)) {
            System.out.println("Кандидат добавлен в голосование!");
        } else {
            System.out.println("Ошибка при добавлении кандидата!");
        }
    }

    private void listActiveVotings() {
        System.out.println("\nАктивные голосования:");
        List<Voting> votings = system.getActiveVotings();
        for (Voting voting : votings) {
            System.out.printf("%s: %s (до %s)%n",
                    voting.getId(), voting.getTitle(), voting.getEndDate());
        }
    }

    private void vote() {
        listActiveVotings();
        String votingId = input("Введите ID голосования");
        Voting voting = system.getVotingById(votingId);

        if (voting == null || !voting.isActive()) {
            System.out.println("Голосование не найдено или завершено!");
            return;
        }

        if (voting.hasUserVoted(system.getCurrentUser().getUsername())) {
            System.out.println("Вы уже голосовали в этом голосовании!");
            return;
        }

        System.out.println("\nКандидаты:");
        for (String candidateId : voting.getCandidateIds()) {
            Candidate candidate = system.getCandidateById(candidateId);
            if (candidate != null) {
                System.out.printf("%s: %s%n", candidate.getId(), candidate);
            }
        }

        String candidateId = input("Введите ID кандидата для голосования");
        if (system.vote(votingId, candidateId, system.getCurrentUser().getUsername())) {
            System.out.println("Ваш голос учтен!");
        } else {
            System.out.println("Ошибка при голосовании!");
        }
    }

    // Методы для работы с кандидатами
    private void createCandidate() {
        String name = input("Имя кандидата");
        String party = input("Партия");
        String bio = input("Биография");
        String id = system.createCandidate(name, party, bio);
        System.out.println("Кандидат создан! ID: " + id);
    }

    private void listCandidates() {
        System.out.println("\nСписок кандидатов:");
        List<Candidate> candidates = system.getAllCandidates();
        for (Candidate candidate : candidates) {
            System.out.printf("%s: %s (%s)%n",
                    candidate.getId(), candidate.getName(), candidate.getParty());
        }
    }

    private void deleteCandidate() {
        String candidateId = input("Введите ID кандидата для удаления");
        if (system.deleteCandidate(candidateId)) {
            System.out.println("Кандидат удален!");
        } else {
            System.out.println("Не удалось удалить кандидата!");
        }
    }

    private String input(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }
}

public class Main {
    public static void main(String[] args) {
        VotingSystem system = new VotingSystem();
        VotingUI ui = new VotingUI(system);
        ui.start();
    }
}
