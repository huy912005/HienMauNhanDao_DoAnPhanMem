const fs = require('fs');
let content = fs.readFileSync('F:/2025-2026/2026/HienMauNhanDao_DoAnPhanMem/XayDungChuongTrinh/PhanMem/FrontEnd/hienmaunhandao/src/pages/nvyt/DonDangKy.jsx', 'utf8');

const startStr = '                  <div>\n                    <label className="text-xs font-semibold text-slate-600 block mb-1">Giới tính</label>';
const endStr = '                  <button onClick={handleCreateTnv}';

const startIdx = content.indexOf(startStr);
const endIdx = content.indexOf(endStr);

if (startIdx > -1 && endIdx > -1) {
    const newChunk =                   <div>
                    <label className="text-xs font-semibold text-slate-600 block mb-1">Giới tính</label>
                    <select value={newTnv.gioiTinh} onChange={e => setNewTnv(p => ({ ...p, gioiTinh: e.target.value }))}
                      className="w-full h-10 border border-slate-200 rounded-lg px-3 text-sm outline-none focus:border-primary">
                      <option>Nam</option><option>\u004e\u1eef</option><option>\u004b\u0068\u00e1\u0063</option>
                    </select>
                  </div>
                  <div>
                    <label className="text-xs font-semibold text-slate-600 block mb-1">\u0050\u0068\u01b0\u1edd\u006e\u0067\u0020\u0078\u00e3 *</label>
                    <select value={newTnv.maPhuongXa} onChange={e => setNewTnv(p => ({ ...p, maPhuongXa: e.target.value }))}
                      className="w-full h-10 border border-slate-200 rounded-lg px-3 text-sm outline-none focus:border-primary">
                      <option value="">\u0043\u0068\u1ecd\u006e\u0020\u0070\u0068\u01b0\u1edd\u006e\u0067\u0020\u0078\u00e3</option>
                      {phuongXaList && phuongXaList.map(px => (<option key={px.maPhuongXa} value={px.maPhuongXa}>{px.tenPhuongXa}</option>))}
                    </select>
                  </div>\n                  <button onClick={handleCreateTnv};
    
    content = content.substring(0, startIdx) + newChunk + content.substring(endIdx + endStr.length);
    fs.writeFileSync('F:/2025-2026/2026/HienMauNhanDao_DoAnPhanMem/XayDungChuongTrinh/PhanMem/FrontEnd/hienmaunhandao/src/pages/nvyt/DonDangKy.jsx', content, 'utf8');
} else {
    console.log('Could not find boundaries.', startIdx, endIdx);
}