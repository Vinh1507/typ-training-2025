// src/services/accountService.ts
export interface BankAccount {
  id: number
  bankName: string
  logo: string
  accountNumber: string
  accountName: string
  balance: string
}

const STORAGE_KEY = "linkedAccounts"

// ✅ Lấy danh sách tài khoản
export const getAccounts = (): BankAccount[] => {
  const data = localStorage.getItem(STORAGE_KEY)
  return data ? JSON.parse(data) : []
}

// ✅ Thêm tài khoản mới
export const addAccount = (account: BankAccount) => {
  const accounts = getAccounts()
  accounts.push(account)
  localStorage.setItem(STORAGE_KEY, JSON.stringify(accounts))
}

// ✅ Xóa tài khoản
export const deleteAccount = (id: number) => {
  const updated = getAccounts().filter(acc => acc.id !== id)
  localStorage.setItem(STORAGE_KEY, JSON.stringify(updated))
}
