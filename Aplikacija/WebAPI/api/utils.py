import base64

def encode_img(img_path):
    base64_string = None
    with open(img_path, 'rb') as img_file:
        base64_string = base64.b64encode(img_file.read())
    return base64_string
