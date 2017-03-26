Novac Password Database.
Try to open test.npdb with password test123456!

DO NOT KEEP SINGLE COPIES OF PASSWORDS IN ONE DB/APP. APP MAY CONTAIN BUGS!
НЕ ХРАНИТЕ ВСЕ ПАРОЛИ В ОДНОЙ БАЗЕ. ПРИЛОЖЕНИЕ МОЖЕТ РАБОТАТЬ С ОШИБКАМИ.

Utility for keeping secure passwords.
Uses .npdb files.
Use menu to add, change or remove passwords.
You can use included password generator to generate strong passwords in change menu.
Click File-Save to save your database or File-Open to open another one.
Be sure to use strong password when saving database.
Closing database through "X"-mark will not damage saved version.
When open databases, choose "backup" to save extra copy of your database before changing it.
Due to JVM, if you want to use app in different OSs with different encodings (like windows-1251 and UTF-8), use latinic characters.


Утилита для безопасного хранения паролей.
Стандартное расширение - .npdb.
Используйте меню для добавления, изменения или удаления паролей.
Вы можете использовать встроенный генератор сильных паролей в меню изменения пароля.
Используйте меню Файл для сохранения текущей или открытия новой базы паролей.
Используйте максимально сильный пароль для сохранения базы данных.
Закрытие базы данных путём закрытия приложения не повредит её содержимого.
Отметьте "backup" при загрузке базы, чтобы сохранить запасную копию этой базы до её изменения.
Из-за отличий в JVM, если вы хотите использовать приложение в разных ОС с разными кодировками (вроде windows-1251 и UTF-8), используйте латиницу.

BUGS:
When opening saved DB with UTF-8 characters in non-default-utf8 OS (like Windows) - it will break your non-utf characters, but it'll be ok next time you open DB in default OS (only if you hasn't saved DB inf different encoding OS - you will lost your text forever)

При открытии БД с символами UTF-8 в системе, в которой UTF-8 не является кодировкой по умолчанию (например, Windows) - вы потеряете символы из нелатинской раскладки. Они откроются нормально в родной ОС БД, но только если вы не сохраните её в другой кодировке - иначе вы потеряете эти записи навсегда.