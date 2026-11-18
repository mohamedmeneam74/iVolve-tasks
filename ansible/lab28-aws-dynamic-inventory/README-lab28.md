<div dir="rtl" lang="ar">
<h2>Lab 28 — AWS EC2 Dynamic Inventory</h2>
<p>الفكرة: بدل ما تكتب الـ <b>inventory</b> بإيدك (زي Lab 24)، الـ <b>plugin</b> ده بيجيب الـ <b>hosts</b> من <b>AWS</b> أوتوماتيك — أي <b>EC2 instance</b> عليها <b>tag</b> اسمه <b>server</b> بقيمة <b>ivolve</b> بتظهر في الـ <b>inventory</b> لوحدها.</p>

<h3>١. التجهيز (من غير حساب AWS — اعمله دلوقتي)</h3>
<pre><code>ansible-galaxy collection install amazon.aws
pip3 install boto3 botocore</code></pre>

<h3>٢. حساب AWS (لما يبقى عندك)</h3>
<p>اعمل حساب AWS، وبعدين <b>IAM user</b> معاه صلاحية قراءة <b>EC2</b> (سياسة <b>AmazonEC2ReadOnlyAccess</b> كفاية للاب ده)، واعمل له <b>access key</b>. ظبط الـ <b>credentials</b> على الـ <b>control plane</b>:</p>
<pre><code>aws configure</code></pre>
<p>أو حطهم في <b>~/.aws/credentials</b>. <b>متبعتش الـ keys في الشات أبداً، ومتحطهاش في Git.</b></p>

<h3>٣. الـ instances</h3>
<p>أي <b>EC2 instance</b> عايزها تظهر في الـ <b>inventory</b> لازم يبقى عليها <b>tag</b>: <b>Key = server</b>، <b>Value = ivolve</b>. الـ <b>Region</b> في ملف <b>aws_ec2.yml</b> هو <b>eu-central-1</b> — غيّره لو الـ <b>instances</b> بتاعتك في <b>region</b> تانية.</p>

<h3>٤. الاختبار</h3>
<pre><code>ansible-inventory -i aws_ec2.yml --graph
ansible-inventory -i aws_ec2.yml --list
ansible -i aws_ec2.yml all -m ping</code></pre>
<p>لو مفيش <b>instances</b> متطابقة، الـ <b>inventory</b> هيطلع فاضي — وده نفسه إثبات إن الـ <b>plugin</b> شغال وبيقرأ من <b>AWS</b>.</p>

<h3>ملحوظة عن التكلفة</h3>
<p>الـ <b>dynamic inventory</b> نفسه ببلاش، بس الـ <b>EC2 instances</b> اللي هيطلعها بتتكلف فلوس حسب نوعها ومدة تشغيلها. متعملش <b>instances</b> إلا لو محتاجها فعلاً، واقفلها بعد ما تخلص.</p>
</div>
