package com.igexin.p022a.p023a.p025d;

/* renamed from: com.igexin.a.a.d.i */
final class C0258i extends Thread {
    volatile boolean f675a;
    C0255f f676b;
    final /* synthetic */ C0243e f677c;

    public C0258i(C0243e c0243e) {
        this.f677c = c0243e;
        this.f675a = true;
        setName("taskService-processor");
    }

    public final void run() {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0089 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:58)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
        /*
        r7 = this;
        r6 = 0;
        r3 = 0;
        r1 = 1;
        r0 = -2;
        android.os.Process.setThreadPriority(r0);
        r0 = r7.f677c;
        r4 = r0.f619m;
        r0 = r1;
        r2 = r3;
    L_0x000d:
        r5 = r7.f675a;
        if (r5 == 0) goto L_0x00c1;
    L_0x0011:
        switch(r0) {
            case -1: goto L_0x0015;
            case 0: goto L_0x0036;
            case 1: goto L_0x005a;
            case 2: goto L_0x00b7;
            default: goto L_0x0014;
        };
    L_0x0014:
        goto L_0x000d;
    L_0x0015:
        r2.m742d();	 Catch:{ Exception -> 0x0033 }
        r0 = r2.m749p();	 Catch:{ Exception -> 0x0033 }
        if (r0 == 0) goto L_0x0036;	 Catch:{ Exception -> 0x0033 }
    L_0x001e:
        r0 = r7.f676b;	 Catch:{ Exception -> 0x0033 }
        if (r0 != 0) goto L_0x002b;	 Catch:{ Exception -> 0x0033 }
    L_0x0022:
        r0 = new com.igexin.a.a.d.f;	 Catch:{ Exception -> 0x0033 }
        r5 = r7.f677c;	 Catch:{ Exception -> 0x0033 }
        r0.<init>(r5);	 Catch:{ Exception -> 0x0033 }
        r7.f676b = r0;	 Catch:{ Exception -> 0x0033 }
    L_0x002b:
        r0 = r7.f676b;	 Catch:{ Exception -> 0x0033 }
        r0.m865a(r2);	 Catch:{ Exception -> 0x0033 }
        r0 = r1;
        r2 = r3;
        goto L_0x000d;
    L_0x0033:
        r0 = move-exception;
        r0 = r1;
        goto L_0x000d;
    L_0x0036:
        r2.a_();	 Catch:{ Exception -> 0x006a, all -> 0x0099 }
        r2.m752s();	 Catch:{ Exception -> 0x006a, all -> 0x0099 }
        r2.m754u();	 Catch:{ Exception -> 0x006a, all -> 0x0099 }
        r0 = r7.f677c;
        r0.m801h();
        r0 = r2.f549E;
        if (r0 != 0) goto L_0x004b;
    L_0x0048:
        r2.m741c();
    L_0x004b:
        r0 = r2.f568w;
        if (r0 != 0) goto L_0x0058;
    L_0x004f:
        r0 = r2.f545A;
        if (r0 != 0) goto L_0x0058;
    L_0x0053:
        r2.f556L = r6;
        r4.m859a(r2);
    L_0x0058:
        r0 = r1;
        r2 = r3;
    L_0x005a:
        r2 = r4.m862c();	 Catch:{ InterruptedException -> 0x00c5 }
    L_0x005e:
        if (r2 == 0) goto L_0x00b7;
    L_0x0060:
        r5 = r2.f568w;
        if (r5 != 0) goto L_0x0068;
    L_0x0064:
        r5 = r2.f569x;
        if (r5 == 0) goto L_0x00b4;
    L_0x0068:
        r2 = r3;
        goto L_0x000d;
    L_0x006a:
        r0 = move-exception;
        r5 = 1;
        r2.f549E = r5;	 Catch:{ Exception -> 0x006a, all -> 0x0099 }
        r2.f557M = r0;	 Catch:{ Exception -> 0x006a, all -> 0x0099 }
        r2.m755v();	 Catch:{ Exception -> 0x006a, all -> 0x0099 }
        r2.m748o();	 Catch:{ Exception -> 0x006a, all -> 0x0099 }
        r0 = r7.f677c;	 Catch:{ Exception -> 0x006a, all -> 0x0099 }
        r0 = r0.f618l;	 Catch:{ Exception -> 0x006a, all -> 0x0099 }
        r0.offer(r2);	 Catch:{ Exception -> 0x006a, all -> 0x0099 }
        r0 = r7.f677c;
        r0.m801h();
        r0 = r2.f549E;
        if (r0 != 0) goto L_0x0089;
    L_0x0086:
        r2.m741c();
    L_0x0089:
        r0 = r2.f568w;
        if (r0 != 0) goto L_0x0096;
    L_0x008d:
        r0 = r2.f545A;
        if (r0 != 0) goto L_0x0096;
    L_0x0091:
        r2.f556L = r6;
        r4.m859a(r2);
    L_0x0096:
        r0 = r1;
        r2 = r3;
        goto L_0x005a;
    L_0x0099:
        r0 = move-exception;
        r1 = r7.f677c;
        r1.m801h();
        r1 = r2.f549E;
        if (r1 != 0) goto L_0x00a6;
    L_0x00a3:
        r2.m741c();
    L_0x00a6:
        r1 = r2.f568w;
        if (r1 != 0) goto L_0x00b3;
    L_0x00aa:
        r1 = r2.f545A;
        if (r1 != 0) goto L_0x00b3;
    L_0x00ae:
        r2.f556L = r6;
        r4.m859a(r2);
    L_0x00b3:
        throw r0;
    L_0x00b4:
        r0 = -1;
        goto L_0x000d;
    L_0x00b7:
        r0 = r2;
        r2 = r7.f677c;
        r2.m801h();
        r2 = r0;
        r0 = r1;
        goto L_0x000d;
    L_0x00c1:
        r4.m863d();
        return;
    L_0x00c5:
        r5 = move-exception;
        goto L_0x005e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.igexin.a.a.d.i.run():void");
    }
}
