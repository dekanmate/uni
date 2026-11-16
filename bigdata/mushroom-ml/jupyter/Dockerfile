FROM apache/spark:4.0.1

USER root

RUN python3 -m pip install --no-cache-dir \
    jupyterlab \
    notebook \
	matplotlib \
	pandas \
	numpy \
	scikit-learn

WORKDIR /home/spark

EXPOSE 8888

CMD ["jupyter", "lab", \
     "--ip=0.0.0.0", \
     "--port=8888", \
     "--no-browser", \
     "--allow-root"]