const fs = require('fs');
let content = fs.readFileSync('F:/2025-2026/2026/HienMauNhanDao_DoAnPhanMem/XayDungChuongTrinh/PhanMem/FrontEnd/hienmaunhandao/src/pages/nvyt/DonDangKy.jsx', 'utf8');

content = content.replace(
    /<div>\s*<label className="text-xs font-semibold text-slate-600 block mb-1">Phu\?ng x. \*<\/label>[\s\S]*?<\/select>\s*<\/div>/,
    <div>
        <label className="text-xs font-semibold text-slate-600 block mb-1">Phường xã *</label>
        <select value={newTnv.maPhuongXa} onChange={e => setNewTnv(p => ({ ...p, maPhuongXa: e.target.value }))}
            className="w-full h-10 border border-slate-200 rounded-lg px-3 text-sm outline-none focus:border-primary">
            <option value="">Chọn phường xã</option>
            {phuongXaList && phuongXaList.length > 0 && phuongXaList.map(px => (<option key={px.maPhuongXa} value={px.maPhuongXa}>{px.tenPhuongXa}</option>))}
        </select>
    </div>
);

fs.writeFileSync('F:/2025-2026/2026/HienMauNhanDao_DoAnPhanMem/XayDungChuongTrinh/PhanMem/FrontEnd/hienmaunhandao/src/pages/nvyt/DonDangKy.jsx', content, 'utf8');