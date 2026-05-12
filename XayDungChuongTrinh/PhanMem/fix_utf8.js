const fs = require('fs');
let content = fs.readFileSync('F:/2025-2026/2026/HienMauNhanDao_DoAnPhanMem/XayDungChuongTrinh/PhanMem/FrontEnd/hienmaunhandao/src/pages/nvyt/DonDangKy.jsx', 'utf8');

// remove corrupted block
content = content.replace(/<div>\s*<label className="text-xs font-semibold text-slate-600 block mb-1">Phu\?ng x \*<\/label>[\s\S]*?<\/select>\s*<\/div>/g, '');

content = content.replace(/<option>Nam<\/option><option>N<\/option><option>Khc<\/option>/g, '<option>Nam</option><option>\u004e\u1eef</option><option>\u004b\u0068\u00e1\u0063</option>');

content = content.replace(/<\/select>\s*<\/div>\s*<button\s+onClick=\{handleCreateTnv\}/g,
</select>
                  </div>
                  <div>
                    <label className="text-xs font-semibold text-slate-600 block mb-1">\u0050\u0068\u01b0\u1edd\u006e\u0067\u0020\u0078\u00e3 *</label>
                    <select value={newTnv.maPhuongXa} onChange={e => setNewTnv(p => ({ ...p, maPhuongXa: e.target.value }))}
                      className="w-full h-10 border border-slate-200 rounded-lg px-3 text-sm outline-none focus:border-primary">
                      <option value="">\u0043\u0068\u1ecd\u006e\u0020\u0070\u0068\u01b0\u1edd\u006e\u0067\u0020\u0078\u00e3</option>
                      {phuongXaList && phuongXaList.map(px => (<option key={px.maPhuongXa} value={px.maPhuongXa}>{px.tenPhuongXa}</option>))}
                    </select>
                  </div>
                  <button onClick={handleCreateTnv}
);

fs.writeFileSync('F:/2025-2026/2026/HienMauNhanDao_DoAnPhanMem/XayDungChuongTrinh/PhanMem/FrontEnd/hienmaunhandao/src/pages/nvyt/DonDangKy.jsx', content, 'utf8');