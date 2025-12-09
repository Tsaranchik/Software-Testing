from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
import time

service = Service(exceutable_path="chromedriver")
driver = webdriver.Chrome(service=service)

try:
	driver.get("https://ulpravda.ru/")
	time.sleep(2)

	for i in range(10):
		print(f"Тестируем ссылку {i + 1}")

		news_links = driver.find_elements(By.CSS_SELECTOR, ".news-article a")

		if i > len(news_links):
			print("Больше ссылок нет")
			break

		link = news_links[i]
		href = link.get_attribute('href')
		link_text = link.text if link.text else "Без текста"

		print(f"\tТекст ссылки: {link_text}")
		print(f"\tURL: {href}")

		if not href:
			print("\tСсылка без href")
			continue

		curr_url = driver.current_url
		
		link.click()

		if driver.current_url != curr_url:
			print("\tПереход по ссылке успешен")

			page_title = driver.title
			print(f"\tЗаголовок страницы: {page_title}")
			time.sleep(3)
			driver.back()
		else:
			print("\tПереход не произошёл")
		
		time.sleep(3)
		print()
except Exception as e:
	print(f"Во время тестирования возникла ошибка:\n{e}")
finally:
	driver.quit()
	print("\nТестирование завершено")