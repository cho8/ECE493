#pragma version(1)
#pragma rs java_package_name(cho8.photowarp)

#include "rs_core.rsh"

#define RS_KERNEL __attribute__((kernel))
const uchar4* input;
int width;
int height;



static uchar4 getPixel(int x, int y) {
	if (y >= height) {
		y = height-1;
	}
	if (y < 0) {
		y = 0;
	}
	if (x >= width) {
		x = width-1;
	}
	if (x < 0) {
		x = 0;
	}
	return input[y*width + x];
}

uchar4 RS_KERNEL bulge(uchar4 in, uint32_t x, uint32_t y) {
	float r, a, rn, cX, cY;

	float xdist, ydist;
	float srcX, srcY;

	cX = (float) width / 2.0f;
	cY = (float) height / 2.0f;

	xdist = ((float) x / (float) width);
	ydist = ((float) y / (float) height);

	r = sqrt(pow((xdist-0.5f), 2) + pow((ydist-0.5f), 2));
	a = atan2((xdist-0.5f), (ydist-0.5f));
	rn = pow(r, 2.0f)/0.5f;

	srcX = (rn * sin(a) + 0.5f) * (float)width;
	srcY = (rn * cos(a) + 0.5f) * (float)height;

	return getPixel((int)srcX, (int)srcY);

}
