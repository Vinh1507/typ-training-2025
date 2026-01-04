import api from "../api";

// 📥 Lấy danh sách tất cả ví
export const getWallets = async () => {
  const res = await api.get("/wallets");
  return res.data;
};

// ➕ Tạo ví mới
export const createWallet = async (wallet: any) => {
  const res = await api.post("/wallets", wallet);
  return res.data;
};

// ✏️ Cập nhật ví
export const updateWallet = async (id: number, wallet: any) => {
  const res = await api.put(`/wallets/${id}`, wallet);
  return res.data;
};

// ❌ Xóa ví
export const deleteWallet = async (id: number) => {
  await api.delete(`/wallets/${id}`);
};

